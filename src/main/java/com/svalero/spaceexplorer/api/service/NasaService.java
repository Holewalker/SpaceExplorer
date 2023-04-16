package com.svalero.spaceexplorer.api.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svalero.spaceexplorer.api.model.APOD;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.time.LocalDate;

public class NasaService {

     private String APOD_URL = "https://api.nasa.gov";
    private ApodAPI apodAPI;

    public NasaService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gsonParser = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit apodAPI = new Retrofit.Builder()
                .baseUrl(APOD_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gsonParser))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.apodAPI = apodAPI.create(ApodAPI.class);
    }
    public Observable<APOD> getAPOD(LocalDate date, String apiKey){
        return this.apodAPI.getAPOD(date, apiKey).map(APOD -> APOD);
    }

}
