package com.ligeirostudio.movieone;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import retrofit2.Call;


public class CustomLoaderCallbacks<T> implements LoaderManager.LoaderCallbacks<T> {

    private final Context context;
    private OnTaskLoaderCompleted onTaskLoaderCompleted;
    private final Call<T> call;

    public CustomLoaderCallbacks(Context context, Call<T> call, OnTaskLoaderCompleted onTaskLoaderCompleted) {
        this.context = context;
        this.call = call;
        this.onTaskLoaderCompleted = onTaskLoaderCompleted;
    }

    @NonNull
    @Override
    public Loader<T> onCreateLoader(int id, @Nullable Bundle args) {

        return new FetchDataLoaderTask<>(context, call);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<T> loader, T data) {
        onTaskLoaderCompleted.loaderCompleted(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<T> loader) {

    }

    public interface OnTaskLoaderCompleted {
        void loaderCompleted(Object data);
    }
}
