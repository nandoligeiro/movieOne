package com.ligeirostudio.movieone.view.home;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ligeirostudio.movieone.R;
import com.ligeirostudio.movieone.database.FavoriteMoveEntity;
import com.ligeirostudio.movieone.databinding.ActivityMainBinding;
import com.ligeirostudio.movieone.model.movie.Result;
import com.ligeirostudio.movieone.model.movie.TheMovie;
import com.ligeirostudio.movieone.retrofit.RequesterApi;
import com.ligeirostudio.movieone.view.moviedetail.DetailsActivity;

import java.util.List;


public class MainActivity extends AppCompatActivity implements MovieTwoAdapter.ListItemClickListener {

    private ActivityMainBinding binding;
    private MovieTwoAdapter movieTwoAdapter;
    private MainViewModel viewModel;

    private int rvPosition = RecyclerView.NO_POSITION;
    private GridLayoutManager gridLayoutManager;
    private final String SCROLL_POSITION_KEY = "scroll_position";
    private static Bundle bundleState;

    private boolean isFavoriteMenuItem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        setupAdapter();

        viewModel.getMovies().observe(this, new Observer<TheMovie>() {
            @Override
            public void onChanged(@Nullable TheMovie theMovie) {
                loadData(theMovie.getResults());
            }
        });

        viewModel.getTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                setTitle(s);
            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        bundleState = new Bundle();
        rvPosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
        bundleState.putInt(SCROLL_POSITION_KEY, rvPosition);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (bundleState != null) {
            rvPosition = bundleState.getInt(SCROLL_POSITION_KEY);
            if (rvPosition == RecyclerView.NO_POSITION) {
                rvPosition = 0;
            }

            binding.recyclerView.smoothScrollToPosition(rvPosition);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putInt(SCROLL_POSITION_KEY, gridLayoutManager.findFirstCompletelyVisibleItemPosition());

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState.containsKey(SCROLL_POSITION_KEY)) {
            rvPosition = savedInstanceState.getInt(SCROLL_POSITION_KEY);
            if (rvPosition == RecyclerView.NO_POSITION) {
                rvPosition = 0;
            }
            binding.recyclerView.smoothScrollToPosition(rvPosition);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    private void setupAdapter() {
        gridLayoutManager = new GridLayoutManager(this, calculateBestSpanCount(350));
        binding.recyclerView.setLayoutManager(gridLayoutManager);
        binding.recyclerView.setHasFixedSize(true);
        movieTwoAdapter = new MovieTwoAdapter(this);
        binding.recyclerView.setAdapter(movieTwoAdapter);
    }

    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }

    @Override
    public void onListItemClick(Result result) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("result", result);
        startActivity(intent);
    }


    private void loadData(List<Result> results) {
        if (results.size() > 0) {
            showMovieDataView();
            movieTwoAdapter.setMovieData(results);
        } else {
            movieTwoAdapter.setMovieData(null);
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
        hideErrorMessage();
        movieTwoAdapter.setMovieData(null);
        switch (item.getItemId()) {
            case R.id.action_popular:
                viewModel.setTitle(getString(R.string.text_most_popular));
                removeFavoriteObserver();
                viewModel.setMovieCall(new RequesterApi().getApi().getMostPopular());
                break;
            case R.id.action_top_rated:
                viewModel.setTitle(getString(R.string.text_top_rated));
                removeFavoriteObserver();
                viewModel.setMovieCall(new RequesterApi().getApi().getTopRated());
                break;
            case R.id.action_my_favorite:
                isFavoriteMenuItem = true;
                viewModel.setTitle(getString(R.string.text_favorites));
                observeFavorites();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void observeFavorites(){
            viewModel.getFavoritesMovies().observe(this, new Observer<List<FavoriteMoveEntity>>() {
                @Override
                public void onChanged(@Nullable List<FavoriteMoveEntity> favoriteMoveEntities) {
                    viewModel.loadFavorites(favoriteMoveEntities);
                }
            });

    }

    private void removeFavoriteObserver(){
        viewModel.getFavoritesMovies().removeObservers(this);

    }

    private void showMovieDataView() {
        hideLoading();
        binding.tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }


    private void showErrorMessage() {
        hideLoading();
        if (isFavoriteMenuItem){
            binding.tvErrorMessageDisplay.setText(R.string.text_no_favorites);
        }else {
            binding.tvErrorMessageDisplay.setText(R.string.error);
        }
        isFavoriteMenuItem = false;
        binding.tvErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage() {
        showLoading();
        binding.tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        binding.pbLoadingPoster.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.pbLoadingPoster.setVisibility(View.INVISIBLE);
    }


}
