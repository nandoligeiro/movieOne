package com.ligeirostudio.movieone.model.video;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Video implements Parcelable {

    @SerializedName("id")
    public Integer id;
    @SerializedName("results")
    public List<VideoResult> results = null;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<VideoResult> getResults() {
        return results;
    }

    public void setResults(List<VideoResult> results) {
        this.results = results;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeList(this.results);
    }

    public Video() {
    }

    protected Video(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.results = new ArrayList<>();
        in.readList(this.results, VideoResult.class.getClassLoader());
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
