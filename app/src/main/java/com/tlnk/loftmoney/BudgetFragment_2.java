package com.tlnk.loftmoney;


import android.content.Intent;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);
        itemsView = view.findViewById(R.id.itemsView);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        itemsView.addItemDecoration(dividerItemDecoration);

        itemsView.setAdapter(moneyCellAdapter_2);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        itemsView.setLayoutManager(layoutManager);

        loadItems();

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

    private void loadItems() {
        Disposable disposable = ((LoftApp) getActivity().getApplication()).moneyApi.getMoneyItems("expense")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoneyResponse>() {
                    @Override
                    public void accept(MoneyResponse s) throws Exception {
                        if (s.getStatus().equals("success")) {
                            for (MoneyRemoteItem moneyRemoteItem : s.getMoneyItemsList()) {
                                moneyItems.add(MoneyItem.getInstance(moneyRemoteItem));
                            }

                            moneyCellAdapter_2.setData(moneyItems);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.connection_lost), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity().getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

}

