package com.tlnk.loftmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.tlnk.loftmoney.cells.MoneyCellAdapter;
import com.tlnk.loftmoney.cells.MoneyItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView itemsView;

    private MoneyCellAdapter moneyCellAdapter = new MoneyCellAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureRecyclerView();

        generateMoney();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        itemsView.addItemDecoration(dividerItemDecoration);
    }

    private void generateMoney() {
        List<MoneyItem> moneyItems = new ArrayList<>();
        moneyItems.add(new MoneyItem("PS5", "46000 ₽"));
        moneyItems.add(new MoneyItem("Salary", "460000 ₽"));
        moneyItems.add(new MoneyItem("PS5", "46000 ₽"));
        moneyItems.add(new MoneyItem("Salary", "460000 ₽"));
        moneyItems.add(new MoneyItem("PS5", "46000 ₽"));
        moneyItems.add(new MoneyItem("Salary", "460000 ₽"));
        moneyItems.add(new MoneyItem("PS5", "46000 ₽"));
        moneyItems.add(new MoneyItem("Salary", "460000 ₽"));
        moneyItems.add(new MoneyItem("PS5", "46000 ₽"));
        moneyItems.add(new MoneyItem("Salary", "460000 ₽"));
        moneyItems.add(new MoneyItem("PS5", "46000 ₽"));
        moneyItems.add(new MoneyItem("Salary", "4600005 ₽"));
        moneyItems.add(new MoneyItem("PS5", "46000 ₽"));
        moneyItems.add(new MoneyItem("Salary", "460000 ₽"));
        moneyItems.add(new MoneyItem("PS5", "46000 ₽"));
        moneyItems.add(new MoneyItem("Salary", "460000 ₽"));
        moneyItems.add(new MoneyItem("PS5", "46000 ₽"));
        moneyItems.add(new MoneyItem("Salary", "460000 ₽"));
        moneyItems.add(new MoneyItem("PS5", "46000 ₽"));
        moneyItems.add(new MoneyItem("Salary", "460000 ₽"));

        moneyCellAdapter.setData(moneyItems);
    }

    @SuppressLint("WrongViewCast")
    private void configureRecyclerView() {
        itemsView = findViewById(R.id.itemsView);
        itemsView.setAdapter(moneyCellAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);

        itemsView.setLayoutManager(layoutManager);
    }
}