package com.ligeirostudio.movieone.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class TheMovie implements Serializable{

    @SerializedName("page")
    private Integer page;
    @SerializedName("total_results")
    private Integer totalResults;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("results")
    private List<Result> results = null;


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

}
