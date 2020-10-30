package com.tlnk.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoneyResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<MoneyRemoteItem> moneyItemsList;

    public String getStatus() {
        return status;
    }

    public List<MoneyRemoteItem> getMoneyItemsList() {
        return moneyItemsList;
    }
}
