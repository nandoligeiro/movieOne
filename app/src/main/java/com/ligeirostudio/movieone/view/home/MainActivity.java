package com.ligeirostudio.movieone.view.home;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.ligeirostudio.movieone.R;
import com.ligeirostudio.movieone.database.FavoriteMoveEntity;
import com.ligeirostudio.movieone.databinding.ActivityMainBinding;
import com.ligeirostudio.movieone.model.movie.Result;
import com.ligeirostudio.movieone.model.movie.TheMovie;
import com.ligeirostudio.movieone.view.generic.FavoriteAdapter;
import com.ligeirostudio.movieone.view.moviedetail.DetailsActivity;

import java.util.List;


public class MainActivity extends AppCompatActivity implements MovieTwoAdapter.ListItemClickListener,
        FavoriteAdapter.FavoriteClickListener {

    private ActivityMainBinding binding;
    private MovieTwoAdapter movieTwoAdapter;
    private FavoriteAdapter favoriteAdapter;
    private static final int LOADER_ID_0 = 0;
    private static final int LOADER_ID_1 = 1;
    private static int currentId = LOADER_ID_0;
    private MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupAdapter();
        setupViewModel();

    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        showLoading();
        switch (currentId) {
            case LOADER_ID_0:
                setTitle(R.string.text_most_popular);
                viewModel.getMostPopular().observe(this, new Observer<Resource<TheMovie>>() {
                    @Override
                    public void onChanged(@Nullable Resource<TheMovie> theMovieResource) {
                        if (theMovieResource != null) {
                            loadData(theMovieResource.getResource());
                        }
                    }
                });

                break;
            case LOADER_ID_1:
                setTitle(R.string.text_top_rated);
                viewModel.getTopRated().observe(this, new Observer<Resource<TheMovie>>() {
                    @Override
                    public void onChanged(@Nullable Resource<TheMovie> theMovieResource) {
                        if (theMovieResource != null) {
                            loadData(theMovieResource.getResource());
                        }
                    }
                });
                break;
        }

        setFavoritesMovie();

    }

    private void setFavoritesMovie() {
        viewModel.getFavoritesMovies().observe(this, new Observer<List<FavoriteMoveEntity>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMoveEntity> favoriteMoveEntities) {
                if (favoriteMoveEntities != null) {
                    if (favoriteMoveEntities.size() > 0) {
                        setupFavoriteAdapter();
                        binding.llFavorites.setVisibility(View.VISIBLE);
                        favoriteAdapter.setData(favoriteMoveEntities);
                    }else {
                        binding.llFavorites.setVisibility(View.GONE);
                    }
                }

            }
        });
    }

    private void setupFavoriteAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerViewFavorites.setLayoutManager(layoutManager);
        binding.recyclerViewFavorites.setHasFixedSize(true);
        favoriteAdapter = new FavoriteAdapter(this);
        binding.recyclerViewFavorites.setAdapter(favoriteAdapter);
    }

    private void setupAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        binding.recyclerView.setHasFixedSize(true);
        movieTwoAdapter = new MovieTwoAdapter(this);
        binding.recyclerView.setAdapter(movieTwoAdapter);
    }


    @Override
    public void onListItemClick(Result result) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("result", result);
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(FavoriteMoveEntity favorite) {

        Result result = new Result();
        result.setId(favorite.getFavoriteId());
        result.setTitle(favorite.getTitle());
        result.setPosterPath(favorite.getPosterPath());
        result.setBackdropPath(favorite.getBackdropPath());
        result.setReleaseDate(favorite.getDate());
        result.setVoteAverage(favorite.getRate());
        result.setOverview(favorite.getOverview());
        result.setFavorite(favorite.isFavorite());

        onListItemClick(result);
    }

    private void loadData(TheMovie movie) {
        if (movie != null) {
            showMovieDataView();
            movieTwoAdapter.setMovieData(movie);
        } else {
            showErrorMessage();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        movieTwoAdapter.setMovieData(null);

        switch (item.getItemId()) {
            case R.id.action_popular:
                currentId = LOADER_ID_0;
                break;
            case R.id.action_top_rated:
                currentId = LOADER_ID_1;
                break;
        }
        setupViewModel();
        return super.onOptionsItemSelected(item);
    }

    private void showMovieDataView() {
        hideLoading();
        binding.tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }


    private void showErrorMessage() {
        hideLoading();
        binding.tvErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        binding.llFavorites.setVisibility(View.GONE);
        binding.pbLoadingPoster.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.pbLoadingPoster.setVisibility(View.INVISIBLE);
    }


}
