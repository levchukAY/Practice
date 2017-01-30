package com.artioml.practice;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.artioml.practice.data.PracticeDatabaseHelper;
import com.artioml.practice.data.DatabaseDescription.History;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Artiom L on 30.01.2017.
 */

public class HistoryActivity extends AppCompatActivity {

    private static final String HISTORY_SETTINGS = "historySettings";
    private static final String HAND = "pref_hand";
    private static final String GLOVES = "pref_gloves";
    private static final String POSITION = "pref_position";
    private static final String PUNCH_TYPE = "pref_punchType";
    private static final String SORT_ORDER = "pref_sortOrder";

    private ArrayList<Result> history;
    private HistoryAdapter adapter;
    private SQLiteDatabase db;

    private String hand;
    private String gloves;
    private String position;
    private int punchType;
    private String sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        fillSettingsPanel();

        history = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(this, history);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDivider(this));

        db = (PracticeDatabaseHelper.getInstance(this)).getReadableDatabase();
        initHistory();

        ImageButton settingsHistoryButton = (ImageButton) findViewById(R.id.settingsHistoryButton);
        settingsHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new HistorySettingsDialog(HistoryActivity.this)).show();
            }
        });

        //MenuItem speedMenuItem = (MenuItem) findViewById(R.id.speed_sort);
        //Drawable icon = speedMenuItem.getIcon();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        //selectedItem = 0;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences sharedPreferences =
                getSharedPreferences(HISTORY_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        switch (item.getItemId()) {
            case (R.id.speed_sort):
                edit.putString(SORT_ORDER, History.COLUMN_SPEED + " DESC");
                break;
            case (R.id.reaction_sort):
                edit.putString(SORT_ORDER, History.COLUMN_REACTION);
                break;
            case (R.id.acceleration_sort):
                edit.putString(SORT_ORDER, History.COLUMN_ACCELERATION + " DESC");
                break;
            case (R.id.date_sort):
                edit.putString(SORT_ORDER, History.COLUMN_DATE + " DESC");
                break;
        }
        edit.apply();

        sortOrder = sharedPreferences.getString(SORT_ORDER, History.COLUMN_DATE + " DESC");
        initHistory();
        /*if (item.getItemId() == R.id.speed_sort ||
                item.getItemId() == R.id.reaction_sort ||
                item.getItemId() == R.id.acceleration_sort ||
                item.getItemId() == R.id.date_sort) {
            if (item.isChecked()) {
                Drawable drawable = item.getIcon();
                drawable.clearColorFilter();
                item.setIcon(drawable);
                item.setIcon(drawable);
                item.setChecked(false);
            }
        }*//*} else {
                Drawable drawable = item.getIcon();
                int color = ContextCompat.getColor(this, R.color.colorGreyDark);
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                drawable = DrawableCompat.wrap(drawable);
                item.setIcon(drawable);
                item.setChecked(true);
            }*/
        /*switch (item.getItemId()) {
            case R.id.vibrate:
            case R.id.dont_vibrate:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        fillSettingsPanel();
        initHistory();
        super.onResume();
    }

    private void initHistory () {

        history.clear();

        ArrayList<String>  values = new ArrayList<>();
        StringBuffer condition = new StringBuffer("");

        if (punchType > 0) {
            condition.append("AND " + History.COLUMN_PUNCH_TYPE + " = ? ");
            values.add((punchType - 1) + "");
        }
        if (hand.compareTo("dm") != 0) {
            condition.append("AND " + History.COLUMN_HAND + " = ? ");
            values.add(hand);
        }
        if (gloves.compareTo("dm") != 0) {
            condition.append("AND " + History.COLUMN_GLOVES + " = ? ");
            values.add(gloves);
        }
        if (position.compareTo("dm") != 0) {
            condition.append("AND position = ? ");
            values.add(position);
        }
        String cond = null;
        String[] vals = null;
        if (condition.toString().compareTo("") != 0) {
            cond = condition.toString().substring(4);
            vals = new String[values.size()];
            for (int i = 0; i < values.size(); i++)
                vals[i] = values.get(i);
        }

        String[] projection = {
                History._ID,
                History.COLUMN_PUNCH_TYPE,
                History.COLUMN_HAND,
                History.COLUMN_GLOVES,
                History.COLUMN_GLOVES_WEIGHT,
                History.COLUMN_POSITION,
                History.COLUMN_SPEED,
                History.COLUMN_REACTION,
                History.COLUMN_ACCELERATION,
                History.COLUMN_DATE
        };
        Cursor cursor = db.query(
                History.TABLE_NAME,     // таблица
                projection,             // столбцы
                cond,                   // столбцы для условия WHERE
                vals,                   // значения для условия WHERE
                null,                   // Don't group the rows
                null,                   // Don't filter by row groups
                sortOrder);             // порядок сортировки

        while (cursor.moveToNext()) {
            history.add(new Result(
                    cursor.getInt(cursor.getColumnIndex(History.COLUMN_PUNCH_TYPE)),
                    cursor.getString(cursor.getColumnIndex(History.COLUMN_HAND)),
                    cursor.getString(cursor.getColumnIndex(History.COLUMN_GLOVES)),
                    cursor.getString(cursor.getColumnIndex(History.COLUMN_GLOVES_WEIGHT)),
                    cursor.getString(cursor.getColumnIndex(History.COLUMN_POSITION)),
                    cursor.getFloat(cursor.getColumnIndex(History.COLUMN_SPEED)),
                    cursor.getFloat(cursor.getColumnIndex(History.COLUMN_REACTION)),
                    cursor.getFloat(cursor.getColumnIndex(History.COLUMN_ACCELERATION)),
                    cursor.getString(cursor.getColumnIndex(History.COLUMN_DATE))
            ));
        }
        cursor.close();

        adapter.notifyDataSetChanged();
    }

    private void fillSettingsPanel(){
        SharedPreferences sharedPreferences =
                getSharedPreferences(HISTORY_SETTINGS, Context.MODE_PRIVATE);

        sortOrder = sharedPreferences.getString(SORT_ORDER, History.COLUMN_DATE + " DESC");

        punchType = sharedPreferences.getInt(PUNCH_TYPE, 0);
        ((TextView) findViewById(R.id.typeHistoryTextView)).setText(getResources().getStringArray(
                R.array.punch_type_history_list)[punchType]);

        hand = sharedPreferences.getString(HAND, "dm");
        ((ImageView) findViewById(R.id.handsHistoryImageView)).setImageDrawable(
                ContextCompat.getDrawable(this, getResources().getIdentifier(
                        "ic_" + hand + "_hand", "drawable", getPackageName())));

        gloves = sharedPreferences.getString(GLOVES, "dm");
        ((ImageView) findViewById(R.id.glovesHistoryImageView)).setImageDrawable(
                ContextCompat.getDrawable(this, getResources().getIdentifier(
                        "ic_gloves_" + gloves, "drawable", getPackageName())));

        position = sharedPreferences.getString(POSITION, "dm");
        ((ImageView) findViewById(R.id.positionHistoryImageView)).setImageDrawable(
                ContextCompat.getDrawable(this, getResources().getIdentifier(
                        "ic_punch_" + position + "_step", "drawable", getPackageName())));
    }

    class ItemDivider extends RecyclerView.ItemDecoration {

        private final Drawable divider;

        // Конструктор загружает встроенный разделитель элементов списка
        public ItemDivider(Context context) {
            int[] attrs = {android.R.attr.listDivider};
            divider = context.obtainStyledAttributes(attrs).getDrawable(0);
        }

        // Рисование разделителей элементов списка в RecyclerView
        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);

            // Вычисление координат x для всех разделителей
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            // Для каждого элемента, кроме последнего, нарисовать линию
            for (int i = 0; i < parent.getChildCount() - 1; ++i) {
                View item = parent.getChildAt(i); // Получить i-й элемент списка

                // Вычисление координат y текущего разделителя
                int top = item.getBottom() + ((RecyclerView.LayoutParams)
                        item.getLayoutParams()).bottomMargin;
                int bottom = top + divider.getIntrinsicHeight();

                // Рисование разделителя с вычисленными границами
                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }
}

