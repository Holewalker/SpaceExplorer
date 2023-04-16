package com.svalero.spaceexplorer.api.service;

import com.svalero.spaceexplorer.api.model.Launch;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpaceDevsAPI {

    @GET("2.2.0/launch/")
    Observable<Launch> getLaunchInfo(@Query("limit") Integer limit,
                                     )
}
