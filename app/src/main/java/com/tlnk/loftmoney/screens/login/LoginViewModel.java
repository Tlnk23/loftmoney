package com.tlnk.loftmoney.screens.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tlnk.loftmoney.remote.AuthApi;
import com.tlnk.loftmoney.remote.AuthResponse;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<String> messageString = new MutableLiveData<>("");
    public MutableLiveData<String> authToken = new MutableLiveData<>("");

    void makeLogin(AuthApi authApi, String userId) {
        compositeDisposable.add (authApi.makeLogin(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authResponse -> {
                    authToken.postValue(authResponse.getAuthToken());
                }, throwable -> {
                    messageString.postValue(throwable.getLocalizedMessage());
                }));
    }
}
