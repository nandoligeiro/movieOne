package com.ligeirostudio.movieone.executor;


import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static final String LOG_TAG = AppExecutors.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static AppExecutors instance;
    private final Executor diskIO;
    private final Executor mainThreadIO;
    private final Executor networkIO;

    public AppExecutors(Executor diskIO, Executor networkIO, Executor mainThreadIO) {
        this.diskIO = diskIO;
        this.mainThreadIO = mainThreadIO;
        this.networkIO = networkIO;
    }


    public static AppExecutors getInstance(){

        if (instance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creanting a new executor");
                instance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }

        Log.d(LOG_TAG, "Getting the executor instance");
        return instance;
    }

    public Executor diskIO(){
        return diskIO;
    }

    public Executor mainThreadIO(){
        return mainThreadIO;
    }

    public Executor networkIO(){
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {

            mainThreadHandler.post(command);

        }
    }
}
