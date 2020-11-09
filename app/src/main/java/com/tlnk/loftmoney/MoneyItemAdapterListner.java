package com.tlnk.loftmoney;

import com.tlnk.loftmoney.cells.MoneyItem;

public interface MoneyItemAdapterListner {

    public void onItemClick(MoneyItem moneyItem, int position);

    public void onItemLongClick(MoneyItem moneyItem, int position);

}