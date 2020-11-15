package com.tlnk.loftmoney.remote;

import com.google.android.gms.common.api.Status;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MoneyApi {

    @GET("./items")
    Single<List<MoneyRemoteItem>> getMoneyItems(@Query("type") String type, @Query("auth-token") String authToken);

    @POST("./items/add")
    @FormUrlEncoded
    Completable postMoney(@Field("price") int price, @Field("name") String name, @Field("type") String type, @Field("auth-token") String authToken);

    @GET("./items/remove")
    Completable removeMoney(@Query("id") int itemId, @Query("auth-token") String authToken);

    @GET("balance")
    Call<BalanceResponce> getBalance(@Query("auth-token") String token);
}
