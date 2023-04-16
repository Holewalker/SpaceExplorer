package com.svalero.spaceexplorer.api.service;

import com.svalero.spaceexplorer.api.model.APOD;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.time.LocalDate;

public interface ApodAPI {

    @GET("planetary/apod")
    Observable<APOD> getAPOD(@Query("date") LocalDate date,
                             @Query("api_key") String apiKey);
}
