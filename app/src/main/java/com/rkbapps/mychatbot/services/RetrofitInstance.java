package com.rkbapps.mychatbot.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiData.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private static ChatApi chatApi = null;

    public static ChatApi getChatApi() {
        if (chatApi == null) {
            chatApi = retrofit.create(ChatApi.class);
        }
        return chatApi;
    }
}
