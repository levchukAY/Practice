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

public class HistorySettingsDialog extends Dialog {

    private static final String HISTORY_SETTINGS = "historySettings";
    private static final String HAND = "pref_hand";
    private static final String GLOVES = "pref_gloves";
    private static final String POSITION = "pref_position";
    private static final String PUNCH_TYPE = "pref_punchType";

    private Context context;
    private Spinner spinner;

    private RadioButton leftHandButton;
    private RadioButton rightHandButton;
    private RadioButton dmHandButton;
    private RadioButton glovesOnButton;
    private RadioButton glovesOffButton;
    private RadioButton glovesDmButton;
    private RadioButton withStepButton;
    private RadioButton withoutStepButton;
    private RadioButton dmStepButton;

    private String hand;
    private String gloves;
    private String position;

    public HistorySettingsDialog(final Context context) {
        //super(context, R.style.MainSettingsTheme);
        super(context);

        this.context = context;
        View contentView = getLayoutInflater().inflate(R.layout.dialog_history_settings, null);
        setContentView(contentView);
        setCancelable(true);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.TOP);

        init();

        /*glovesOnButton.setOnCheckedChangeListener(onCheckedChangeListener);
        glovesOffButton.setOnCheckedChangeListener(onCheckedChangeListener);

        rightHandButton.setOnCheckedChangeListener(onCheckedChangeListener);
        leftHandButton.setOnCheckedChangeListener(onCheckedChangeListener);

        withoutStepButton.setOnCheckedChangeListener(onCheckedChangeListener);
        withStepButton.setOnCheckedChangeListener(onCheckedChangeListener);
*/
        setChecked();

        findViewById(R.id.menuTopHistoryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitChanges();
                dismiss();
            }
        });

        setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((HistoryActivity) context).onResume();
            }
        });
    }

    /*final CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    switch (compoundButton.getId()) {
                        case (R.id.leftHandButton):
                            changeFilter(leftHandButton, rightHandButton, !b);
                            break;
                        case (R.id.rightHandButton):
                            changeFilter(leftHandButton, rightHandButton, b);
                            break;
                        case (R.id.withStepButton):
                            changeFilter(withStepButton, withoutStepButton, !b);
                            break;
                        case (R.id.withoutStepButton):
                            changeFilter(withStepButton, withoutStepButton, b);
                            break;
                        case (R.id.glovesOnButton):
                            changeFilter(glovesOffButton, glovesOnButton, b);
                            break;
                        case (R.id.glovesOffButton):
                            changeFilter(glovesOffButton, glovesOnButton, !b);
                            break;
                    }
                }
            };

    private void changeFilter(RadioButton firstButton, RadioButton secondButton, boolean b) {
        if (b) applyFilter(firstButton, secondButton);
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
    }*/

    private void init() {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(HISTORY_SETTINGS, Context.MODE_PRIVATE);
        hand = sharedPreferences.getString(HAND, "dm");
        gloves = sharedPreferences.getString(GLOVES, "dm");
        position = sharedPreferences.getString(POSITION, "dm");

        leftHandButton = (RadioButton) findViewById(R.id.leftHandHistoryButton);
        rightHandButton = (RadioButton) findViewById(R.id.rightHandHistoryButton);
        dmHandButton = (RadioButton) findViewById(R.id.dmHandHistoryButton);
        glovesOnButton = (RadioButton) findViewById(R.id.glovesOnHistoryButton);
        glovesOffButton = (RadioButton) findViewById(R.id.glovesOffHistoryButton);
        glovesDmButton = (RadioButton) findViewById(R.id.glovesDmHistoryButton);
        withStepButton = (RadioButton) findViewById(R.id.withStepHistoryButton);
        withoutStepButton = (RadioButton) findViewById(R.id.withoutStepHistoryButton);
        dmStepButton = (RadioButton) findViewById(R.id.dmStepHistoryButton);

        spinner = (Spinner) findViewById(R.id.punchTypeHistorySpinner);
        spinner.setSelection(sharedPreferences.getInt(PUNCH_TYPE, 0));
    }

    private void setChecked() {
        if (hand.compareTo("left") == 0)
            leftHandButton.setChecked(true);
        else if (hand.compareTo("right") == 0)
            rightHandButton.setChecked(true);
        else dmHandButton.setChecked(true);

        if (gloves.compareTo("on") == 0)
            glovesOnButton.setChecked(true);
        else if (gloves.compareTo("off") == 0)
            glovesOffButton.setChecked(true);
        else glovesDmButton.setChecked(true);

        if (position.compareTo("with") == 0)
            withStepButton.setChecked(true);
        else if (position.compareTo("without") == 0)
            withoutStepButton.setChecked(true);
        else dmStepButton.setChecked(true);
    }

    private void commitChanges(){
        context.getSharedPreferences(HISTORY_SETTINGS, Context.MODE_PRIVATE)
                .edit()
                .putString(HAND, leftHandButton.isChecked() ? "left" :
                        rightHandButton.isChecked() ?"right" : "dm")
                .putString(GLOVES, glovesOnButton.isChecked() ? "on" :
                        glovesOffButton.isChecked() ? "off" : "dm")
                .putString(POSITION, withStepButton.isChecked() ? "with" :
                        withoutStepButton.isChecked() ? "without" : "dm")
                .putInt(PUNCH_TYPE, spinner.getSelectedItemPosition())
                .apply();
    }

}
