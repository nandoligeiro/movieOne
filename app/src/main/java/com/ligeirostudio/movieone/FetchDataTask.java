package com.ligeirostudio.movieone;

import android.os.AsyncTask;

import java.io.IOException;

import retrofit2.Call;


public class FetchDataTask<T> extends AsyncTask<Call, Void, T> {

    private OnTaskCompleted onTaskCompleted;

    public FetchDataTask(OnTaskCompleted activityContext){
        this.onTaskCompleted = activityContext;
    }

    @Override
    protected T doInBackground(Call[] calls) {
        if (calls.length == 0) {
            return null;
        }
        try {
            Call<T> objectCall = calls[0];
            return objectCall.execute().body();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(T data) {
        super.onPostExecute(data);
        onTaskCompleted.onTaskCompleted(data);
    }


    public interface OnTaskCompleted {
        void onTaskCompleted(Object data);
    }
}
