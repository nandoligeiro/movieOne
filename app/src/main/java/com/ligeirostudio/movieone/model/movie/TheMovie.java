package com.ligeirostudio.movieone.model.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class TheMovie implements Parcelable {

    @SerializedName("page")
    private Integer page;
    @SerializedName("total_results")
    private Integer totalResults;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("results")
    private List<Result> results = new ArrayList<>();


    public Integer getPage() {
        return page;
    }


    public Integer getTotalResults() {
        return totalResults;
    }


    public Integer getTotalPages() {
        return totalPages;
    }


    public List<Result> getResults() {
        return results;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.page);
        dest.writeValue(this.totalResults);
        dest.writeValue(this.totalPages);
        dest.writeList(this.results);
    }

    public TheMovie() {
    }

    protected TheMovie(Parcel in) {
        this.page = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalResults = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalPages = (Integer) in.readValue(Integer.class.getClassLoader());
        this.results = new ArrayList<>();
        in.readList(this.results, Result.class.getClassLoader());
    }

    public static final Creator<TheMovie> CREATOR = new Creator<TheMovie>() {
        @Override
        public TheMovie createFromParcel(Parcel source) {
            return new TheMovie(source);
        }

        @Override
        public TheMovie[] newArray(int size) {
            return new TheMovie[size];
        }
    };
}
