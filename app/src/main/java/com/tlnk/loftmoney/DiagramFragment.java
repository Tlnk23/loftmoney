package com.tlnk.loftmoney;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.api.Api;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tlnk.loftmoney.cells.MoneyItem;
import com.tlnk.loftmoney.remote.BalanceResponce;
import com.tlnk.loftmoney.remote.MoneyApi;
import com.tlnk.loftmoney.remote.MoneyRemoteItem;
import com.tlnk.loftmoney.remote.MoneyResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiagramFragment  extends Fragment {
    private MoneyApi mApi;
    private TextView myExpences;
    private TextView myIncome;
    private TextView totalFinances;
    private BalanceView mDiagramView;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi = ((LoftApp) getActivity().getApplication()).moneyApi;
        loadBalance();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_balance, null);
        myExpences = view.findViewById(R.id.my_expences);
        myIncome = view.findViewById(R.id.my_income);
        totalFinances = view.findViewById(R.id.txtBalanceFinanceValue);
        mDiagramView = view.findViewById(R.id.balanceView);

        return view;
    }

    public void loadBalance() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), 0);
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");

        final Call<BalanceResponce> responceCall = mApi.getBalance(authToken);
        responceCall.enqueue(new Callback<BalanceResponce>() {

            @Override
            public void onResponse(
                    final Call<BalanceResponce> call, final Response<BalanceResponce> response
            ) {

                final float totalExpences = response.body().getTotalExpences();
                final float totalIncome = response.body().getTotalIncome();

                myExpences.setText(String.valueOf(totalExpences));
                myIncome.setText(String.valueOf(totalIncome));
                totalFinances.setText(String.valueOf(totalIncome - totalExpences));
                mDiagramView.update(totalExpences, totalIncome);

            }

            @Override
            public void onFailure(final Call<BalanceResponce> call, final Throwable t) {

            }
        });
    }

}

