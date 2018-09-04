package com.ligeirostudio.movieone.view.moviedetail;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ligeirostudio.movieone.database.AppDatabase;


public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final AppDatabase db;
    private final int favoriteId;


    public DetailsViewModelFactory(AppDatabase db, int favoriteId) {
        this.db = db;
        this.favoriteId = favoriteId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsViewModel(db, favoriteId);

    }
}
