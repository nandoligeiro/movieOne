package com.ligeirostudio.movieone.view.generic;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ligeirostudio.movieone.Constants;
import com.ligeirostudio.movieone.R;
import com.ligeirostudio.movieone.database.FavoriteMoveEntity;
import com.ligeirostudio.movieone.databinding.ItemPosterBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    final private FavoriteClickListener onClickListener;
    private List<FavoriteMoveEntity> results = new ArrayList<>();

    private ItemPosterBinding binding;


    public FavoriteAdapter(FavoriteClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setData(List<FavoriteMoveEntity> results) {
        this.results = results;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_poster, parent, false);
        return new FavoriteViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.bind(results.get(position).getPosterPath());
    }

    @Override
    public int getItemCount() {
        return results.size() > 0 ? results.size() : 0;
    }

    public interface FavoriteClickListener {
        void onFavoriteClick(FavoriteMoveEntity result);
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(String posterPath) {
            Picasso.get()
                    .load(Constants.POSTER_URL + Constants.SIZE_W342 + posterPath)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(binding.ivPoster);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onFavoriteClick(results.get(getAdapterPosition()));

        }
    }

}
