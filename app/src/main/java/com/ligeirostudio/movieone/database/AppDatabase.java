package com.ligeirostudio.movieone.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {FavoriteMoveEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movietwo";
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context){

        if (instance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creanting a new database");
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }

        Log.d(LOG_TAG, "Getting the database instance");
        return instance;
    }

    public abstract FavoriteDAO favoriteDAO();

}
