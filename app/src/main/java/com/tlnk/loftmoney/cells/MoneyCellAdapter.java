package com.tlnk.loftmoney.cells;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tlnk.loftmoney.MoneyItemAdapterListner;
import com.tlnk.loftmoney.R;

import java.util.List;
import java.util.ArrayList;

public class MoneyCellAdapter extends RecyclerView.Adapter<MoneyCellAdapter.MoneyViewHolder> {

    private List<MoneyItem> moneyItemList = new ArrayList<>();
    private MoneyItemAdapterListner mListner;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void toggleItem(final int position) {
        if (selectedItems.get(position) == true) {
            selectedItems.put(position, false);
        } else  {
            selectedItems.put(position, true);
        }

        notifyDataSetChanged();
    }

    public int getSelectedSize() {
        int result = 0;
        for (int i = 0; i < moneyItemList.size(); i++) {
            if (selectedItems.get(i)) {
                result++;
            }
        }
        return result;
    }

    public List<Integer> getSelectedItemId() {
        List<Integer> result = new ArrayList<>();
        int i = 0;
        for (MoneyItem item: moneyItemList) {
            if (selectedItems.get(i)) {
                result.add(item.getId());
            }
            i++;
        }
        return result;
    }

    public void setListner(final MoneyItemAdapterListner listner) {
        mListner = listner;
    }

    public void clearItems() {
        moneyItemList.clear();
        notifyDataSetChanged();
    }

    public void setData(List<MoneyItem> moneyItems) {
            moneyItemList.clear();
            moneyItemList.addAll(moneyItems);

            notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoneyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MoneyViewHolder(layoutInflater.inflate(R.layout.cell_money, parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull final MoneyViewHolder holder, final int position) {
        holder.bind(moneyItemList.get(position), selectedItems.get(position));
        holder.setListner(mListner, moneyItemList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return moneyItemList.size();
    }

    static class MoneyViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView valueTextView;
        private View mItemView;

        public MoneyViewHolder(@NonNull final View itemView) {
            super(itemView);

            mItemView = itemView;
            titleTextView = itemView.findViewById(R.id.moneyCellTitleView);
            valueTextView = itemView.findViewById(R.id.priceText);
        }

        public void bind(final MoneyItem moneyItem, final boolean isSelected) {
            itemView.setSelected(isSelected);
            titleTextView.setText(moneyItem.getTitle());
            valueTextView.setText((moneyItem.getValue()));
        }

        public void setListner(final MoneyItemAdapterListner listner, final MoneyItem moneyItem, final int position) {
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    listner.onItemClick(moneyItem, position);
                }
            });

            mItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    listner.onItemLongClick(moneyItem, position);
                    return false;
                }
            });
        }
    }




}
