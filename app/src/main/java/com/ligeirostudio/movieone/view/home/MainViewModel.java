package com.ligeirostudio.movieone.view.home;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.ligeirostudio.movieone.database.AppDatabase;
import com.ligeirostudio.movieone.database.FavoriteMoveEntity;
import com.ligeirostudio.movieone.model.movie.TheMovie;
import com.ligeirostudio.movieone.retrofit.RequesterApi;

import java.util.List;


public class MainViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MainViewModel.class.getSimpleName();


    private LiveData<List<FavoriteMoveEntity>> dbMovies;

    LiveData<Resource<TheMovie>> mostPopular, topRated;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(LOG_TAG, "Retrieving favorites from database");
        dbMovies = database.favoriteDAO().loadFavorites();

        mostPopular = new RequesterApi().getApi().getMostPopular();
        topRated = new RequesterApi().getApi().getTopRated();

    }

    public LiveData<List<FavoriteMoveEntity>> getFavoritesMovies() {
        return dbMovies;
    }

    public LiveData<Resource<TheMovie>> getMostPopular() {
        return mostPopular;
    }

    public LiveData<Resource<TheMovie>> getTopRated() {
        return topRated;
    }
}
