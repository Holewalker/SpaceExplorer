package com.svalero.spaceexplorer.api.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpaceDevsService {

     private String SPACEDEVS_URL = "https://ll.thespacedevs.com";
    private SpaceDevsAPI spaceDevsAPI;

    public SpaceDevsService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gsonParser = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit apodAPI = new Retrofit.Builder()
                .baseUrl(SPACEDEVS_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gsonParser))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.spaceDevsAPI = spaceDevsAPI.create(SpaceDevsAPI.class);
    }

}
