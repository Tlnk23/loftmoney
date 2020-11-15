package com.tlnk.loftmoney.cells;

import com.tlnk.loftmoney.remote.MoneyRemoteItem;

public class MoneyItem {

    private String title;
    private String value;
    private int id;

    public MoneyItem(String title, String value) {
        this.title = title;
        this.value = value;
    }



    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }



    public static MoneyItem  getInstance(MoneyRemoteItem moneyRemoteItem) {
        return new MoneyItem(moneyRemoteItem.getName(), moneyRemoteItem.getPrice() + "₽");
    }
}
