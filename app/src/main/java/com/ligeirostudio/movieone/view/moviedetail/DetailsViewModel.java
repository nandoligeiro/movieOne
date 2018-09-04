package com.ligeirostudio.movieone.view.moviedetail;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.ligeirostudio.movieone.database.AppDatabase;
import com.ligeirostudio.movieone.database.FavoriteMoveEntity;
import com.ligeirostudio.movieone.model.review.Review;
import com.ligeirostudio.movieone.model.video.Video;
import com.ligeirostudio.movieone.retrofit.RequesterApi;

import java.util.List;


public class DetailsViewModel extends ViewModel {


    LiveData<List<FavoriteMoveEntity>> listFavorites;
    LiveData<Resource<Video>> video;
    LiveData<Resource<Review>> review;



    public DetailsViewModel(AppDatabase db, int favoriteId) {

        listFavorites = db.favoriteDAO().loadFavorites();
        video = new RequesterApi().getApi().getVideo(favoriteId);
        review = new RequesterApi().getApi().getReview(favoriteId);
    }

    public LiveData<List<FavoriteMoveEntity>> getListFavorites() {
        return listFavorites;
    }

    public LiveData<Resource<Video>> getVideo() {
        return video;
    }

    public LiveData<Resource<Review>> getReview() {
        return review;
    }
}
