package com.tlnk.loftmoney.remote;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MoneyApi {

    @GET("./items")
    Single<MoneyResponse> getMoneyItems(@Query("type") String type);

    @POST("./items/add")
    @FormUrlEncoded
    Completable postMoney(@Field("price") int price, @Field("name") String name, @Field("type") String type);
}
