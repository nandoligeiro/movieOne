package com.ligeirostudio.movieone.view.moviedetail;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ligeirostudio.movieone.R;
import com.ligeirostudio.movieone.databinding.ItemTrailerBinding;
import com.ligeirostudio.movieone.model.video.VideoResult;

import java.util.ArrayList;
import java.util.List;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    final private TrailerClickListener onClickListener;
    private List<VideoResult> videoResults = new ArrayList<>();


    public TrailerAdapter(TrailerClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setData(List<VideoResult> results) {
        this.videoResults = results;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemTrailerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_trailer, parent, false);
        return new TrailerViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return videoResults.size() > 0 ? videoResults.size() : 0;
    }

    public interface TrailerClickListener {
        void onTrailerClick(VideoResult result);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TrailerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onTrailerClick(videoResults.get(getAdapterPosition()));

        }
    }

}
