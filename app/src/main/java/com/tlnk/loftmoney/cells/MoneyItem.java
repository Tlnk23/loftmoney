package com.tlnk.loftmoney.cells;

public class MoneyItem {

    private String title;
    private String value;

    public MoneyItem(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }
}
