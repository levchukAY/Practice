package com.artioml.practice;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class LicenseActivity extends AppCompatActivity {

    private static final String PERSISTENT_STORAGE = "persistentStorage";
    private static final String IS_FIRST_TIME = "pref_isFirstTime";
    private static final String IS_ACCEPTED = "pref_isAccepted";

    private Button acceptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        acceptButton = (Button) findViewById(R.id.acceptButton);

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(IS_FIRST_TIME, true)) {
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        if (getSharedPreferences(PERSISTENT_STORAGE, MODE_PRIVATE).getBoolean(IS_ACCEPTED, false)) {
            acceptButton.setVisibility(View.INVISIBLE);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        acceptButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getSharedPreferences(PERSISTENT_STORAGE, MODE_PRIVATE)
                        .edit()
                        .putBoolean(IS_ACCEPTED, true)
                        .apply();
                finish();
            }
        });

        final View line = findViewById(R.id.lineView);
        line.post(new Runnable() {
            @Override
            public void run() {
                BitmapDrawable parallel = drawParallelogramLine(line.getWidth());
                line.setBackground(parallel);
            }
        });

    }

    public BitmapDrawable drawParallelogramLine(int width){
        Matrix matrix = new Matrix();
        Path path = new Path();
        path.addRect(0, 0, (4 * width) / 40, (4 * width) / 200, Path.Direction.CW);
        Path pathStamp = new Path();
        Paint p;
        Bitmap bitmap;

        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.FILL);

        bitmap = Bitmap.createBitmap(width / 4, width / 80 * 2 + (4 * width) / 200,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        p.setColor(ContextCompat.getColor(this, R.color.colorPrimaryLight));

        matrix.reset();
        matrix.setTranslate(0,0);
        matrix.postSkew(-1f, 0.0f, 0, (4 * width) / 200);
        path.transform(matrix, pathStamp);
        canvas.drawPath(pathStamp, p);

        p.setColor(ContextCompat.getColor(this, R.color.colorAccent));

        matrix.reset();
        matrix.setTranslate(width / 8, 0);
        matrix.postSkew(-1f, 0.0f, width / 8, (4 * width) / 200);
        path.transform(matrix, pathStamp);
        canvas.drawPath(pathStamp, p);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        bitmapDrawable.setTileModeX(Shader.TileMode.REPEAT);

        return bitmapDrawable;
    }

}
