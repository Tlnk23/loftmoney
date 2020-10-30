package com.tlnk.loftmoney.remote;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoneyApi {

    @GET("./items")
    Single<MoneyResponse> getMoneyItems(@Query("type") String type);
}
