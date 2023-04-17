package com.svalero.spaceexplorer.api.service;

import com.svalero.spaceexplorer.api.model.Launch;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.time.LocalDateTime;

public interface SpaceDevsAPI {

    @GET("2.2.0/launch")
    Observable<Launch> getLaunchInfo(@Query("limit") Integer limit,
                                     @Query("window_start__gte") LocalDateTime windowStart,
                                     @Query("window_end__lte") LocalDateTime windowEnd

    );
}
