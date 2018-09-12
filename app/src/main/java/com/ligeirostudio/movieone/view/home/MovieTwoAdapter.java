package com.ligeirostudio.movieone.view.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ligeirostudio.movieone.Constants;
import com.ligeirostudio.movieone.R;
import com.ligeirostudio.movieone.model.movie.Result;
import com.ligeirostudio.movieone.model.movie.TheMovie;
import com.squareup.picasso.Picasso;

import java.util.List;


class MovieTwoAdapter extends RecyclerView.Adapter<MovieTwoAdapter.MovieOneViewHolder> {

    final private ListItemClickListener onClickListener;
    private List<Result> results;

    public MovieTwoAdapter(ListItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MovieTwoAdapter.MovieOneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster, parent, false);
        return new MovieOneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTwoAdapter.MovieOneViewHolder holder, int position) {
        holder.bind(results.get(position).getPosterPath());
    }

    @Override
    public int getItemCount() {
        return results != null && results.size() > 0 ? results.size() : 0;
    }

    public void setMovieData(List<Result> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    public class MovieOneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;

        public MovieOneViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onClickListener.onListItemClick(results.get(getAdapterPosition()));

        }

        public void bind(String posterPath) {
            Picasso.get()
                    .load(Constants.POSTER_URL + Constants.SIZE_W342 + posterPath)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(Result result);
    }
}
