package com.artioml.practice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_SETTINGS = "mainSettings";
    private static final String HAND = "pref_hand";
    private static final String GLOVES = "pref_gloves";
    private static final String GLOVES_WEIGHT = "pref_glovesWeight";
    private static final String POSITION = "pref_position";
    private static final String PUNCH_TYPE = "pref_punchType";
    private static final String IS_FIRST_TIME = "pref_isFirstTime";

    private MainSettingsDialog mainSettingsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainSettingsDialog = new MainSettingsDialog(this);

        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainSettingsDialog.show();
            }
        });

        fillSettingsPanel();

        Button punchButton = (Button) findViewById(R.id.punchButton);
        punchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent punchResultIntent = new Intent(MainActivity.this, PunchResultActivity.class);

                float speed = (float) Math.random() * 100;
                float reaction = (float) Math.random() * 100;
                float acceleration = (float) Math.random() * 100;

                punchResultIntent.putExtra("speed", speed);
                punchResultIntent.putExtra("reaction", reaction);
                punchResultIntent.putExtra("acceleration", acceleration);

                startActivity(punchResultIntent);
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
        super.onResume();
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
