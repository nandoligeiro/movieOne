package com.ligeirostudio.movieone.retrofit;



import com.ligeirostudio.movieone.model.TheMovie;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiService {

    @GET("movie/popular")
    Call<TheMovie> getMostPopular();

    @GET("movie/top_rated")
    Call<TheMovie> getTopRated();

}
