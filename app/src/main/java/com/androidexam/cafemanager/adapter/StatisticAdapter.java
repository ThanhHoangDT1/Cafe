package com.androidexam.cafemanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidexam.cafemanager.R;
import com.androidexam.cafemanager.model.Oder;
import com.androidexam.cafemanager.model.OderDetail;

import java.util.List;

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder> {
    private List<Oder> statisticsList;

    public StatisticAdapter(List<Oder> statisticsList) {
        this.statisticsList = statisticsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Oder item = statisticsList.get(position);

    }

    @Override
    public int getItemCount() {
        return statisticsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView monthTextView;
        TextView revenueTextView;
        TextView topProductTextView;
        TextView quantityTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
