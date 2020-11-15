package com.tlnk.loftmoney;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.api.Status;
import com.tlnk.loftmoney.cells.MoneyCellAdapter;
import com.tlnk.loftmoney.cells.MoneyItem;
import com.tlnk.loftmoney.remote.MoneyApi;
import com.tlnk.loftmoney.remote.MoneyRemoteItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetFragment extends Fragment implements MoneyItemAdapterListner, ActionMode.Callback {

    private RecyclerView itemsView;
    private MoneyCellAdapter moneyCellAdapter = new MoneyCellAdapter();
    private List<MoneyItem> moneyItems = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ActionMode mActionMode;
    private MoneyApi mApi;

    private static final String ARG_PARAM1 = "param1";
    private int fragmentIndex;
    private String fragmentType;

    public static BudgetFragment newInstance(int param1) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView priceColor = (TextView) getActivity().findViewById(R.id.priceText);

        fragmentIndex = getArguments().getInt(ARG_PARAM1);
        if (fragmentIndex == 0) {
            fragmentType = "income";
            priceColor.setTextColor(Color.parseColor("#3574fa"));
        } else {
            fragmentType = "expense";
            priceColor.setTextColor(Color.parseColor("#7ed321"));
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), 0);
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");

        View view = inflater.inflate(R.layout.fragment_budget, null);
        itemsView = view.findViewById(R.id.itemsView);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        itemsView.addItemDecoration(dividerItemDecoration);
        itemsView.setAdapter(moneyCellAdapter);
        moneyCellAdapter.setListner(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        itemsView.setLayoutManager(layoutManager);

        loadItems(authToken);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadItems(authToken);
            }
        });
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
        moneyCellAdapter.setData(moneyItems);
    }

    private void loadItems(String authToken) {
        Disposable disposable = ((LoftApp) getActivity().getApplication()).moneyApi.getMoneyItems(fragmentType, authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moneyRemoteItems -> {
                        mSwipeRefreshLayout.setRefreshing(false);
                        moneyItems.clear();
                        for (MoneyRemoteItem moneyRemoteItem : moneyRemoteItems) {
                            moneyItems.add(MoneyItem.getInstance(moneyRemoteItem));
                        }
                            for (int i = 0; i < moneyItems.size() / 2; i++) {
                                MoneyItem tmp = moneyItems.get(i);
                                moneyItems.set(i, moneyItems.get(moneyItems.size() - i - 1));
                                moneyItems.set(moneyItems.size() - i - 1, tmp);
                            }
                        moneyCellAdapter.setData(moneyItems);

                }, throwable -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity().getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                });
        compositeDisposable.add(disposable);
    }

    private void removeMoney() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), 0);
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");
        List<Integer> selectedItems = moneyCellAdapter.getSelectedItemId();

        for (Integer itemId : selectedItems) {
            Disposable disposable = ((LoftApp) getActivity().getApplication()).moneyApi.removeMoney(itemId, authToken)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        loadItems(authToken);
                        moneyCellAdapter.clearSelections();
                    }, throwable -> {
                        Log.e("ERROR",throwable.getLocalizedMessage());
                    });
            compositeDisposable.add(disposable);
        }
    }

    @Override
    public void onItemClick(MoneyItem moneyItem, int position) {
        moneyCellAdapter.toggleItem(position);

        if (mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(moneyCellAdapter.getSelectedSize())));
        }
    }

    @Override
    public void onItemLongClick(MoneyItem moneyItem, int position) {
        if (mActionMode == null) {
            getActivity().startActionMode(this);
        }
        moneyCellAdapter.toggleItem(position);

        if (mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(moneyCellAdapter.getSelectedSize())));
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        mActionMode = actionMode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getActivity());
        menuInflater.inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.remove) {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.confirmation)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeMoney();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mActionMode = null;
        moneyCellAdapter.clearSelections();
    }
}


