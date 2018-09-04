package com.ligeirostudio.movieone;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;

import retrofit2.Call;


public class FetchDataLoaderTask<T> extends AsyncTaskLoader<T> {

    private Call<T> call = null;
    private T data;

    public FetchDataLoaderTask(Context context, Call<T> call) {
        super(context);
        this.call = call;
    }


    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        } else {
            forceLoad();
        }

    }

    @Nullable
    @Override
    public T loadInBackground() {
        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deliverResult(T data) {
        this.data = data;
        if (isStarted()) {
            super.deliverResult(data);
        }

    }


}
