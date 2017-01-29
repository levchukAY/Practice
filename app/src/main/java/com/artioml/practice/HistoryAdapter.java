package com.artioml.practice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Artiom L on 29.01.2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<Result> history; // Поисковые запросы
    private Context context;

    public HistoryAdapter(Context context, List<Result> history) {
        this.history = history;
        this.context = context;
    }

    // Вложенный субкласс RecyclerView.ViewHolder используется для
    // реализации паттерна View-Holder в контексте RecyclerView--
    // логики повторного использования представлений
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView punchTypeTextView;
        private final TextView dateTextView;
        private final TextView speedTextView;
        private final TextView reactionTextView;
        private final TextView accelerationTextView;
        private final TextView weightTextView;

        private final ImageView handsImageView;
        private final ImageView glovesImageView;
        private final ImageView positionImageView;

        // Настройка объекта ViewHolder элемента RecyclerView
        private ViewHolder(View itemView) {
            super(itemView);

            punchTypeTextView = (TextView) itemView.findViewById(R.id.punchTypeHistoryTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateHistoryTextView);
            speedTextView = (TextView) itemView.findViewById(R.id.speedHistoryTextView);
            reactionTextView = (TextView) itemView.findViewById(R.id.reactionHistoryTextView);
            accelerationTextView = (TextView) itemView.findViewById(R.id.accelerationHistoryTextView);
            weightTextView = (TextView) itemView.findViewById(R.id.weightHistoryTextView);

            handsImageView = (ImageView) itemView.findViewById(R.id.handsHistoryImageView);
            glovesImageView = (ImageView) itemView.findViewById(R.id.glovesHistoryImageView);
            positionImageView = (ImageView) itemView.findViewById(R.id.positionHistoryImageView);
        }
    }

    // Создает новый элемент списка и его объект ViewHolder
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Заполнение макета list_item
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.history_item, parent, false);

        // Создание ViewHolder для текущего элемента
        return (new ViewHolder(view));
    }

    // Назначение текста элемента списка для вывода тега запроса
    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {

        Result currentResult = history.get(position);

        holder.punchTypeTextView.setText(context.getResources().getStringArray(
                R.array.punch_type_list)[currentResult.punchType]);
        holder.dateTextView.setText(currentResult.date);
        holder.speedTextView.setText(context.getString(R.string.speed_result, currentResult.speed));
        holder.reactionTextView.setText(
                context.getString(R.string.reaction_result, currentResult.reaction));
        holder.accelerationTextView.setText(
                context.getString(R.string.acceleration_result, currentResult.acceleration));
        holder.weightTextView.setText(currentResult.glovesWeight);

        String hand = "ic_" + currentResult.hand + "_hand";
        holder.handsImageView.setImageDrawable(ContextCompat.getDrawable(context,
                context.getResources().getIdentifier(hand, "drawable", context.getPackageName())));

        String gloves = "ic_gloves_" + currentResult.gloves;
        holder.glovesImageView.setImageDrawable(ContextCompat.getDrawable(context,
                context.getResources().getIdentifier(gloves, "drawable", context.getPackageName())));

        if (currentResult.gloves.compareTo("on") == 0)
            holder.weightTextView.setVisibility(View.VISIBLE);
        else holder.weightTextView.setVisibility(View.GONE);

        String pos = "ic_punch_" +  currentResult.position + "_step";
        holder.positionImageView.setImageDrawable(ContextCompat.getDrawable(context,
                context.getResources().getIdentifier(pos, "drawable", context.getPackageName())));
    }

    @Override
    public int getItemCount() {
        return history.size();
    }
}
