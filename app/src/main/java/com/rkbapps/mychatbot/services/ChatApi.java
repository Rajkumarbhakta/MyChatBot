package com.rkbapps.mychatbot.services;

import com.rkbapps.mychatbot.models.BodyModel;
import com.rkbapps.mychatbot.models.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChatApi {
    @Headers({"content-type: application/json; charset=utf-8", "X-RapidAPI-Key: " + ApiData.X_RapidAPI_Key, "X-RapidAPI-Host: " + ApiData.X_RapidAPI_Host})
    @POST("ask")
    Call<ResponseModel> getBotResponse(@Body BodyModel body);

}
