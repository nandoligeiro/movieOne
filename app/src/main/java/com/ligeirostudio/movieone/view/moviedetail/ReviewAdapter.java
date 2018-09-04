package com.ligeirostudio.movieone.view.moviedetail;


import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ligeirostudio.movieone.R;
import com.ligeirostudio.movieone.databinding.ItemReviewBinding;
import com.ligeirostudio.movieone.model.review.ReviewResult;

import java.util.ArrayList;
import java.util.List;




public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewResult> reviewResults = new ArrayList<>();

    private ItemReviewBinding binding;


    public void setData(List<ReviewResult> results) {
        this.reviewResults = results;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_review, parent, false);
        return new ReviewViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        binding.tvReview.setText(reviewResults.get(position).getContent());
        binding.tvAuthor.setText(reviewResults.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviewResults.size() > 0 ? reviewResults.size() : 0;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        public ReviewViewHolder(View itemView) {
            super(itemView);
        }

    }

}
