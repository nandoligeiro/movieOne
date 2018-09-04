package com.ligeirostudio.movieone.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavoriteMoveEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int favoriteId;
    private String title;
    private String posterPath;
    private String backdropPath;
    private Float rate;
    private String date;
    private String overview;
    private boolean isFavorite;

    @Ignore
    public FavoriteMoveEntity(int id) {
        this.id = id;
        this.isFavorite = isFavorite;
    }
    @Ignore
    public FavoriteMoveEntity(int favoriteId, String title, String posterPath, String backdropPath, Float rate, String date, String overview, boolean isFavorite) {
        this.favoriteId = favoriteId;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.rate = rate;
        this.date = date;
        this.overview = overview;
        this.isFavorite = isFavorite;
    }

    public FavoriteMoveEntity(int id, int favoriteId, String title, String posterPath, String backdropPath, Float rate, String date, String overview, boolean isFavorite) {
        this.id = id;
        this.favoriteId = favoriteId;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.rate = rate;
        this.date = date;
        this.overview = overview;
        this.isFavorite = isFavorite;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
