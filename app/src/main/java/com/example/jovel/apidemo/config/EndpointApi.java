package com.example.jovel.apidemo.config;

import com.example.jovel.apidemo.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EndpointApi {

    @GET("locations/{location-id}")
    Call<ApiResponse> getLocations(@Path("location-id")int id,
                                   @Query("access_token") String token);

}
