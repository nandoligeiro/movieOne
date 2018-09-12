package com.ligeirostudio.movieone.view.home;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ligeirostudio.movieone.R;
import com.ligeirostudio.movieone.database.AppDatabase;
import com.ligeirostudio.movieone.database.FavoriteMoveEntity;
import com.ligeirostudio.movieone.model.movie.Result;
import com.ligeirostudio.movieone.model.movie.TheMovie;
import com.ligeirostudio.movieone.retrofit.RequesterApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MainViewModel.class.getSimpleName();


    private LiveData<List<FavoriteMoveEntity>> dbMovies;

    private MutableLiveData<List<FavoriteMoveEntity>> favoritesMovie = new MutableLiveData<>();


    private MutableLiveData<TheMovie> movie;

    private MutableLiveData<Call<TheMovie>> movieCall = new MutableLiveData<>();

    private MutableLiveData<String> title = new MutableLiveData<>();

    private AppDatabase database;

    public MainViewModel(@NonNull Application application) {
        super(application);

        database = AppDatabase.getInstance(application);
        dbMovies = database.favoriteDAO().loadFavorites();
        favoritesMovie.setValue(dbMovies.getValue());
        movieCall.setValue(new RequesterApi().getApi().getMostPopular());
        title.setValue(application.getString(R.string.text_most_popular));

    }

    public LiveData<List<FavoriteMoveEntity>> getFavoritesMovies() {
        return dbMovies;
    }

    public LiveData<TheMovie> getMovies() {
        if (movie == null) {
            movie = new MutableLiveData<>();
            loadMovie();
        }

        return movie;
    }

    public void loadFavorites(List<FavoriteMoveEntity> favoriteMoveEntities){
        Log.d(LOG_TAG, "Retrieving favorites from database");

        TheMovie theMovie = new TheMovie();
        List<Result> results = new ArrayList<>();
        if (dbMovies.getValue() != null){
            for (FavoriteMoveEntity favorite : favoriteMoveEntities){
                results.add(createModelResult(favorite));
            }

        }

        theMovie.setResults(results);
        movie.setValue(theMovie);

    }

    private Result createModelResult(FavoriteMoveEntity favorite) {
        Result result = new Result();
        result.setId(favorite.getFavoriteId());
        result.setTitle(favorite.getTitle());
        result.setPosterPath(favorite.getPosterPath());
        result.setBackdropPath(favorite.getBackdropPath());
        result.setReleaseDate(favorite.getDate());
        result.setVoteAverage(favorite.getRate());
        result.setOverview(favorite.getOverview());
        result.setFavorite(favorite.isFavorite());

        return result;
    }



    public void setMovieCall(Call<TheMovie> call) {
        movieCall.setValue(call);
        loadMovie();
    }

    public LiveData<String> getTitle(){
        return title;
    }
    public void setTitle(String title) {
       this.title.setValue(title);
    }


    private void loadMovie() {

        movieCall.getValue().enqueue(new Callback<TheMovie>() {
            @Override
            public void onResponse(Call<TheMovie> call, Response<TheMovie> response) {
                movie.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TheMovie> call, Throwable t) {
                movie.setValue(new TheMovie());

            }
        });

    }

}
