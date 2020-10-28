package com.tlnk.loftmoney.cells;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tlnk.loftmoney.R;

import java.util.ArrayList;
import java.util.List;

public class MoneyCellAdapter_2 extends RecyclerView.Adapter<MoneyCellAdapter_2.MoneyViewHolder> {
    private Button addButton;
    private List<MoneyItem> moneyItemList = new ArrayList<>();



    public void setData(List<MoneyItem> moneyItems) {
            moneyItemList.clear();
            moneyItemList.addAll(moneyItems);

            notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MoneyViewHolder(layoutInflater.inflate(R.layout.cell_money_2, parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MoneyViewHolder holder, int position) {
        holder.bind(moneyItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return moneyItemList.size();
    }

    static class MoneyViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView valueTextView;

        public MoneyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.moneyCellTitleView_2);
            valueTextView = itemView.findViewById(R.id.moneyCellValueView_2);
        }

        public void bind(MoneyItem moneyItem) {
            titleTextView.setText(moneyItem.getTitle());
            valueTextView.setText((moneyItem.getValue()));
        }
    }
}
