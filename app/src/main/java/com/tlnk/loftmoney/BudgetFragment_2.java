package com.tlnk.loftmoney;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tlnk.loftmoney.cells.MoneyCellAdapter;
import com.tlnk.loftmoney.cells.MoneyCellAdapter_2;
import com.tlnk.loftmoney.cells.MoneyItem;
import com.tlnk.loftmoney.remote.MoneyRemoteItem;
import com.tlnk.loftmoney.remote.MoneyResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BudgetFragment_2 extends Fragment {

    private RecyclerView itemsView;
    private MoneyCellAdapter_2 moneyCellAdapter_2 = new MoneyCellAdapter_2();
    private List<MoneyItem> moneyItems = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), 0);
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");

        View view = inflater.inflate(R.layout.fragment_budget, null);
        itemsView = view.findViewById(R.id.itemsView);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        itemsView.addItemDecoration(dividerItemDecoration);

        itemsView.setAdapter(moneyCellAdapter_2);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        itemsView.setLayoutManager(layoutManager);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems(authToken);
            }
        });

        loadItems(authToken);

        return view;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String nameAdd = data.getStringExtra("name");
        String priceAdd = data.getStringExtra("price");

        moneyItems.add(new MoneyItem(nameAdd, priceAdd + " ₽"));
        moneyCellAdapter_2.setData(moneyItems);

    }

    private void loadItems(String authToken) {
        Disposable disposable = ((LoftApp) getActivity().getApplication()).moneyApi.getMoneyItems("expense", authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moneyRemoteItems -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    moneyCellAdapter_2.clearItems();
                    for (MoneyRemoteItem moneyRemoteItem : moneyRemoteItems) {
                        moneyItems.add(MoneyItem.getInstance(moneyRemoteItem));
                    }

                    for (int i = 0; i < moneyItems.size() / 2; i++) {
                        MoneyItem tmp = moneyItems.get(i);
                        moneyItems.set(i, moneyItems.get(moneyItems.size() - i - 1));
                        moneyItems.set(moneyItems.size() - i - 1, tmp);
                    }

                    moneyCellAdapter_2.setData(moneyItems);

                }, throwable -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity().getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                });
        compositeDisposable.add(disposable);
    }

}

