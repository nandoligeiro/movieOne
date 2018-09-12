package com.ligeirostudio.movieone.view.moviedetail;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ligeirostudio.movieone.database.AppDatabase;
import com.ligeirostudio.movieone.database.FavoriteMoveEntity;
import com.ligeirostudio.movieone.model.movie.Result;


public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final AppDatabase db;
    private final Result result;


    public DetailsViewModelFactory(AppDatabase db, Result result) {
        this.db = db;
        this.result = result;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsViewModel(db, result);

    }
}
