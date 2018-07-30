package com.ligeirostudio.movieone.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class Result implements Serializable {

    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("id")
    private Integer id;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
    private Float voteAverage;
    @SerializedName("title")
    private String title;
    @SerializedName("popularity")
    private Float popularity;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("genre_ids")
    private List<Long> genreIds = null;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("adult")
    private Boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;


    public Integer getVoteCount() {
        return voteCount;
    }


    public Integer getId() {
        return id;
    }


    public Boolean getVideo() {
        return video;
    }


    public Float getVoteAverage() {
        return voteAverage;
    }


    public String getTitle() {
        return title;
    }


    public Float getPopularity() {
        return popularity;
    }


    public String getPosterPath() {
        return posterPath;
    }


    public String getOriginalLanguage() {
        return originalLanguage;
    }


    public String getOriginalTitle() {
        return originalTitle;
    }


    public List<Long> getGenreIds() {
        return genreIds;
    }


    public String getBackdropPath() {
        return backdropPath;
    }


    public Boolean getAdult() {
        return adult;
    }


    public String getOverview() {
        return overview;
    }


    public String getReleaseDate() {
        return releaseDate;
    }

}
