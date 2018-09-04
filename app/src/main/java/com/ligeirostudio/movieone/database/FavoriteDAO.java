package com.ligeirostudio.movieone.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoriteDAO {

    @Query("SELECT * FROM favorites")
    LiveData<List<FavoriteMoveEntity>> loadFavorites();

    @Query("SELECT * FROM favorites WHERE favoriteId = :id")
    FavoriteMoveEntity getByFavoriteId(int id);

    @Insert
    void insertFavorite(FavoriteMoveEntity favoriteMoveEntity);

    @Query("DELETE FROM favorites WHERE favoriteId = :id")
    void deleteByFavoriteId(int id);
}
