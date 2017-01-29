package com.artioml.practice;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Artiom L on 27.01.2017.
 */

public class MainSettingsDialog extends Dialog {

    private static final String MAIN_SETTINGS = "mainSettings";
    private static final String HAND = "pref_hand";
    private static final String GLOVES = "pref_gloves";
    private static final String GLOVES_WEIGHT = "pref_glovesWeight";
    private static final String POSITION = "pref_position";
    private static final String PUNCH_TYPE = "pref_punchType";

    private Context context;
    private EditText weightEditText;
    private Spinner spinner;

    private RadioButton leftHandButton;
    private RadioButton rightHandButton;
    private RadioButton glovesOnButton;
    private RadioButton glovesOffButton;
    private RadioButton withStepButton;
    private RadioButton withoutStepButton;

    private String hand;
    private String gloves;
    private String position;

    public MainSettingsDialog(final Context context) {
        super(context, R.style.MainSettingsTheme);

        this.context = context;
        View contentView = getLayoutInflater().inflate(R.layout.dialog_main_settings, null);
        setContentView(contentView);
        setCancelable(true);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.TOP);

        init();

        glovesOnButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            View glovesWeight = findViewById(R.id.glovesWeight);
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeFilter(glovesOffButton, glovesOnButton, b);
                if (b)
                    glovesWeight.setVisibility(View.VISIBLE);
                else
                    glovesWeight.setVisibility(View.GONE);
            }
        });

        rightHandButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeFilter(leftHandButton, rightHandButton, b);
            }
        });

        withoutStepButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeFilter(withStepButton, withoutStepButton, b);
            }
        });

        findViewById(R.id.menuTopButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitChanges();
                dismiss();
            }
        });

        setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((MainActivity) context).onResume();
            }
        });

        setChecked();
    }

    private void changeFilter(RadioButton firstButton, RadioButton secondButton, boolean b) {
        if (b)
            applyFilter(firstButton, secondButton);
        else applyFilter(secondButton, firstButton);
    }

    private void applyFilter(RadioButton firstButton, RadioButton secondButton) {
        Drawable drawable = firstButton.getCompoundDrawables()[1];
        int color = ContextCompat.getColor(context, R.color.colorGreyDark);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        drawable = DrawableCompat.wrap(drawable);
        firstButton.setCompoundDrawables(null, drawable, null, null);

        drawable = secondButton.getCompoundDrawables()[1];
        drawable.clearColorFilter();
        secondButton.setCompoundDrawables(null, drawable, null, null);
    }

    private void init() {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(MAIN_SETTINGS, Context.MODE_PRIVATE);
        hand = sharedPreferences.getString(HAND, "right");
        gloves = sharedPreferences.getString(GLOVES, "on");
        position = sharedPreferences.getString(POSITION, "without");

        leftHandButton = (RadioButton) findViewById(R.id.leftHandButton);
        rightHandButton = (RadioButton) findViewById(R.id.rightHandButton);
        glovesOnButton = (RadioButton) findViewById(R.id.glovesOnButton);
        glovesOffButton = (RadioButton) findViewById(R.id.glovesOffButton);
        withStepButton = (RadioButton) findViewById(R.id.withStepButton);
        withoutStepButton = (RadioButton) findViewById(R.id.withoutStepButton);

        weightEditText = (EditText) findViewById(R.id.weightEditText);
        weightEditText.setText(sharedPreferences.getString(GLOVES_WEIGHT, "80"));

        spinner = (Spinner) findViewById(R.id.punchTypeSpinner);
        spinner.setSelection(sharedPreferences.getInt(PUNCH_TYPE, 0));
    }

    private void setChecked() {
        if (hand.compareTo("left") == 0)
            leftHandButton.setChecked(true);
        else rightHandButton.setChecked(true);

        if (gloves.compareTo("on") == 0)
            glovesOnButton.setChecked(true);
        else glovesOffButton.setChecked(true);

        if (position.compareTo("with") == 0)
            withStepButton.setChecked(true);
        else withoutStepButton.setChecked(true);
    }

    private void commitChanges(){
        context.getSharedPreferences(MAIN_SETTINGS, Context.MODE_PRIVATE)
                .edit()
                .putString(HAND, leftHandButton.isChecked() ? "left" : "right")
                .putString(GLOVES, glovesOnButton.isChecked() ? "on" : "off")
                .putString(POSITION, withStepButton.isChecked() ? "with" : "without")
                .putString(GLOVES_WEIGHT, weightEditText.getText().toString())
                .putInt(PUNCH_TYPE, spinner.getSelectedItemPosition())
                .apply();
    }

}
