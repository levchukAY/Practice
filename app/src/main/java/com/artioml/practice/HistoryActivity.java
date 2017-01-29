package com.artioml.practice;

import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
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

import com.artioml.practice.data.PracticeDatabaseHelper;
import com.artioml.practice.data.DatabaseDescription.History;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<Result> history;

    private HistoryAdapter adapter;

    private int selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        history = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(this, history);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDivider(this));

        initHistory();

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

    private void initHistory () {

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

        SQLiteDatabase db = (PracticeDatabaseHelper.getInstance(this)).getReadableDatabase();
        Cursor cursor = db.query(
                History.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

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

        Collections.reverse(history);
        adapter.notifyDataSetChanged();
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
