package com.ligeirostudio.movieone.view.moviedetail;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.ligeirostudio.movieone.database.AppDatabase;
import com.ligeirostudio.movieone.database.FavoriteMoveEntity;
import com.ligeirostudio.movieone.executor.AppExecutors;
import com.ligeirostudio.movieone.model.movie.Result;
import com.ligeirostudio.movieone.model.review.Review;
import com.ligeirostudio.movieone.model.video.Video;
import com.ligeirostudio.movieone.retrofit.RequesterApi;

import java.util.List;


public class DetailsViewModel extends ViewModel {

    private Result result;
    private AppDatabase db;

    private LiveData<Resource<Video>> video;
    private LiveData<Resource<Review>> review;


    public DetailsViewModel(AppDatabase db, Result result) {

        this.result = result;
        this.db = db;
        video = new RequesterApi().getApi().getVideo(result.getId());
        review = new RequesterApi().getApi().getReview(result.getId());

    }

    public LiveData<List<FavoriteMoveEntity>> getListFavorites() {
        return db.favoriteDAO().loadFavorites();
    }

    public LiveData<Resource<Video>> getVideo() {
        return video;
    }

    public LiveData<Resource<Review>> getReview() {
        return review;
    }

    public LiveData<FavoriteMoveEntity> getFavoriteById(){
        return db.favoriteDAO().getByFavoriteId(result.getId());
    }

    public void saveFavorite(final FavoriteMoveEntity favorite){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                db.favoriteDAO().insertFavorite(favorite);

            }
        });
    }

    public void deleteFavorite(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.favoriteDAO().deleteByFavoriteId(result.getId());
            }
        });
    }
}
