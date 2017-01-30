package com.artioml.practice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_SETTINGS = "mainSettings";
    private static final String HAND = "pref_hand";
    private static final String GLOVES = "pref_gloves";
    private static final String GLOVES_WEIGHT = "pref_glovesWeight";
    private static final String POSITION = "pref_position";
    private static final String PUNCH_TYPE = "pref_punchType";
    private static final String IS_FIRST_TIME = "pref_isFirstTime";

    private final int ACCELERATION_THRESSHOLD = 20;
    private final int REACTION_THRESSHOLD = 10;

    private Button punchButton;

    private SoundPool soundPool;
    private SparseIntArray soundMap;

    private float currentAcceleration;
    private float maxAcceleration;
    private float reaction;
    private float count;

    private ArrayList<Float> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainSettingsDialog mainSettingsDialog = new MainSettingsDialog(MainActivity.this);
                mainSettingsDialog.show();
            }
        });

        fillSettingsPanel();

        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setUsage(AudioAttributes.USAGE_GAME);

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(1);
        builder.setAudioAttributes(attrBuilder.build());

        soundPool = builder.build();

        soundMap = new SparseIntArray(1);
        soundMap.put(0, soundPool.load(getBaseContext(), R.raw.fire, 1));

        punchButton = (Button) findViewById(R.id.punchButton);
        punchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                punchButton.setEnabled(false);
                (new Handler()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        soundPool.play(soundMap.get(0), 1, 1, 1, 0, 1f);

                        reaction = 0.00f;
                        maxAcceleration = 0;
                        count = 0;

                        data = new ArrayList<>();

                        SensorManager sensorManager =
                                (SensorManager) getSystemService(Context.SENSOR_SERVICE);

                        sensorManager.registerListener(sensorEventListener,
                                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                                1000);
                    }
                }, 2000);
            }
        });

        Button historyButton = (Button) findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent licenceIntent = new Intent(this, LicenseActivity.class);
        startActivity(licenceIntent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean(IS_FIRST_TIME, true)) {
            Intent licenseIntent = new Intent(this, LicenseActivity.class);
            startActivity(licenseIntent);
            sharedPreferences.edit().putBoolean(IS_FIRST_TIME, false).apply();
        }
    }

    @Override
    protected void onResume() {
        fillSettingsPanel();
        punchButton.setEnabled(true);
        super.onResume();
    }

    @Override
    protected  void onPause() {
        super.onPause();

        SensorManager sensorManager =
                (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensorManager.unregisterListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
    }
    private final SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            count++;
            float x = event.values[0] ;
            float y = event.values[1];
            float z = event.values[2];

            // Сохранить предыдущие данные ускорения
            //lastAcceleration = currentAcceleration;

            // Вычислить текущее ускорение
            currentAcceleration = (float) Math.sqrt(x * x + y * y + z * z);

            if (currentAcceleration > 5)
                data.add(currentAcceleration);

            // Вычислить изменение ускорения
            //acceleration = currentAcceleration * (currentAcceleration - lastAcceleration);

            maxAcceleration = Math.max(maxAcceleration, currentAcceleration);
            //m = Math.max(m, currentAcceleration);

            if (reaction == 0 && currentAcceleration > REACTION_THRESSHOLD)
                reaction = count / 50;

            if (maxAcceleration > ACCELERATION_THRESSHOLD && currentAcceleration < 5) {

                Intent punchResultIntent = new Intent(MainActivity.this, PunchResultActivity.class);

                punchResultIntent.putExtra("speed", countSpeed(data));
                punchResultIntent.putExtra("reaction", reaction);
                punchResultIntent.putExtra("acceleration", maxAcceleration);

                startActivity(punchResultIntent);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    private float countSpeed(ArrayList<Float> data) {
        float speed = 0.00f;
        Collections.reverse(data);
        data.remove(0);
        for (Float f : data) {
            speed += f;
            if (f < 3)
                return speed / 50;
        }
        return speed / 50;
    }

    private void fillSettingsPanel(){
        SharedPreferences sharedPreferences = getSharedPreferences(MAIN_SETTINGS, Context.MODE_PRIVATE);

        ((TextView) findViewById(R.id.punchTypeView)).setText(getResources().getStringArray(
                R.array.punch_type_list)[sharedPreferences.getInt(PUNCH_TYPE, 0)]);

        String hand = "ic_" + sharedPreferences.getString(HAND, "right") + "_hand";
        ((TextView) findViewById(R.id.handView)).setCompoundDrawablesWithIntrinsicBounds(null,
                ContextCompat.getDrawable(MainActivity.this,
                        getResources().getIdentifier(hand, "drawable", getPackageName())), null, null);

        String gloves = "ic_gloves_" + sharedPreferences.getString(GLOVES, "off");
        ((TextView) findViewById(R.id.glovesView)).setCompoundDrawablesWithIntrinsicBounds(null,
                ContextCompat.getDrawable(MainActivity.this,
                        getResources().getIdentifier(gloves, "drawable", getPackageName())), null, null);

        if (gloves.compareTo("ic_gloves_off") == 0)
            ((TextView) findViewById(R.id.glovesView)).setText("");
        else
            ((TextView) findViewById(R.id.glovesView)).setText(sharedPreferences.getString(GLOVES_WEIGHT, "80"));

        String position = "ic_punch_" +  sharedPreferences.getString(POSITION, "with") + "_step";
        ((TextView) findViewById(R.id.positionView)).setCompoundDrawablesWithIntrinsicBounds(null,
                ContextCompat.getDrawable(MainActivity.this,
                        getResources().getIdentifier(position, "drawable", getPackageName())), null, null);
    }

}
