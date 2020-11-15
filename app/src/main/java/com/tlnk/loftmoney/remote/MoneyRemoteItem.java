package com.tlnk.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

public class MoneyRemoteItem {

    @SerializedName("id")
    private String itemId;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("type")
    private String type;

    @SerializedName("date")
    private String date;

    @SerializedName("total_expenses")
    private String total_expenses;

    @SerializedName("total_income")
    private String total_income;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpenses() {
        return total_expenses;
    }

    public String getIncome() {
        return total_income;
    }
}
