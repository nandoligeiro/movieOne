package com.ligeirostudio.movieone;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ligeirostudio.movieone.model.Result;
import com.ligeirostudio.movieone.model.TheMovie;
import com.squareup.picasso.Picasso;


class MovieOneAdapter extends RecyclerView.Adapter<MovieOneAdapter.MovieOneViewHolder> {

    final private ListItemClickListener onClickListener;
    private TheMovie movie;

    MovieOneAdapter(ListItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MovieOneAdapter.MovieOneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster, parent, false);
        return new MovieOneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieOneAdapter.MovieOneViewHolder holder, int position) {
        holder.bind(movie.getResults().get(position).getPosterPath());
    }

    @Override
    public int getItemCount() {
        return movie != null ? movie.getResults().size() : 0;
    }

    public void setMovieData(TheMovie movieData) {
        movie = movieData;
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
            onClickListener.onListItemClick(movie.getResults().get(getAdapterPosition()));

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
