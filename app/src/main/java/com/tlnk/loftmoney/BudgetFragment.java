package com.tlnk.loftmoney;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.tlnk.loftmoney.cells.MoneyCellAdapter;
import com.tlnk.loftmoney.cells.MoneyItem;

import java.util.ArrayList;
import java.util.List;

public class BudgetFragment extends Fragment {
    private FloatingActionButton btnAdd;
    private RecyclerView itemsView;

    private MoneyCellAdapter moneyCellAdapter = new MoneyCellAdapter();
    private List<MoneyItem> moneyItems = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.expences));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.income));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.balance));

        btnAdd = view.findViewById(R.id.addNewExpense);

        configureRecyclerView();

        generateMoney(moneyItems);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        itemsView.addItemDecoration(dividerItemDecoration);



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String nameAdd = data.getStringExtra("name");
        String priceAdd = data.getStringExtra("price") + " ₽";

        moneyItems.add(new MoneyItem(nameAdd, priceAdd));
        moneyCellAdapter.setData(moneyItems);
    }


    private void generateMoney(List<MoneyItem> moneyItems) {
        moneyCellAdapter.setData(moneyItems);
    }

    @SuppressLint("WrongViewCast")
    private void configureRecyclerView() {
        itemsView = findViewById(R.id.itemsView);
        itemsView.setAdapter(moneyCellAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        itemsView.setLayoutManager(layoutManager);
    }

}
