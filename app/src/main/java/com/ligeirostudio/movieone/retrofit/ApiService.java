package com.ligeirostudio.movieone.retrofit;


import android.arch.lifecycle.LiveData;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.ligeirostudio.movieone.model.movie.TheMovie;
import com.ligeirostudio.movieone.model.review.Review;
import com.ligeirostudio.movieone.model.video.Video;

import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ApiService {

    @GET("movie/popular")
    LiveData<Resource<TheMovie>> getMostPopular();

    @GET("movie/top_rated")
    LiveData<Resource<TheMovie>> getTopRated();

    @GET("movie/{id}/videos")
    LiveData<Resource<Video>> getVideo(@Path("id") int movieId);

    @GET("movie/{id}/reviews")
    LiveData<Resource<Review>> getReview(@Path("id") int reviewId);
}
