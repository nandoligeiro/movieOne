package com.ligeirostudio.movieone;

import android.os.AsyncTask;

import com.ligeirostudio.movieone.model.TheMovie;

import java.io.IOException;

import retrofit2.Call;


public class FetchDataMovieTask extends AsyncTask<Call, Void, TheMovie> {

    private OnTaskCompleted onTaskCompleted;

    public FetchDataMovieTask(OnTaskCompleted activityContext){
        this.onTaskCompleted = activityContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected TheMovie doInBackground(Call... calls) {

        if (calls.length == 0) {
            return null;
        }
        try {
            Call<TheMovie> objectCall = calls[0];
            return objectCall.execute().body();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(TheMovie movie) {
        super.onPostExecute(movie);
        onTaskCompleted.onTaskCompleted(movie);
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(TheMovie movie);
    }
}
