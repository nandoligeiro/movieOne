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

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.ligeirostudio.movieone.R;
import com.ligeirostudio.movieone.database.FavoriteMoveEntity;
import com.ligeirostudio.movieone.databinding.ActivityMainBinding;
import com.ligeirostudio.movieone.model.movie.Result;
import com.ligeirostudio.movieone.model.movie.TheMovie;
import com.ligeirostudio.movieone.view.moviedetail.DetailsActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MovieTwoAdapter.ListItemClickListener {

    private ActivityMainBinding binding;
    private MovieTwoAdapter movieTwoAdapter;
    private static final int MOST_POPULAR_ID_0 = 0;
    private static final int TOP_RATED_ID_1 = 1;
    private static final int MY_FAVORITE_ID_2 = 2;
    private static int currentId = MOST_POPULAR_ID_0;
    private MainViewModel viewModel;

    private int rvPosition = RecyclerView.NO_POSITION;
    private GridLayoutManager gridLayoutManager;
    private final String SCROLL_POSITION_KEY = "scroll_position";
    private static Bundle bundleState;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        setupAdapter();
        setupViewModel();

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

        outState.putInt(SCROLL_POSITION_KEY,  gridLayoutManager.findFirstCompletelyVisibleItemPosition());

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


    private void setupViewModel() {

        showLoading();
        switch (currentId) {
            case MOST_POPULAR_ID_0:
                setMostPopularMovie();
                break;
            case TOP_RATED_ID_1:
                setTopRatedMovie();
                break;

            case MY_FAVORITE_ID_2:
                setFavoritesMovie();
                break;
        }

    }

    private void setFavoritesMovie() {
        setTitle(R.string.text_favorites);
        viewModel.getFavoritesMovies().observe(this, new Observer<List<FavoriteMoveEntity>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMoveEntity> favoriteMoveEntities) {

                if (favoriteMoveEntities != null) {
                    if (favoriteMoveEntities.size() > 0) {
                        prepareData(favoriteMoveEntities);
                    }
                }else {
                    showErrorMessage();
                }

            }

        });
    }


    private void setMostPopularMovie() {
        setTitle(R.string.text_most_popular);
        viewModel.getMostPopular().observe(this, new Observer<Resource<TheMovie>>() {
            @Override
            public void onChanged(@Nullable Resource<TheMovie> theMovieResource) {
                if (theMovieResource.getResource() != null) {
                    if (theMovieResource.getResource().getResults().size() > 0) {
                        loadData(theMovieResource.getResource().getResults());
                    }
                }else  {
                    viewModel.getMostPopular().removeObserver(this);
                    showErrorMessage();
                }
            }
        });
    }

    private void setTopRatedMovie() {
        setTitle(R.string.text_top_rated);
        viewModel.getTopRated().observe(this, new Observer<Resource<TheMovie>>() {
            @Override
            public void onChanged(@Nullable Resource<TheMovie> theMovieResource) {
                if (theMovieResource.getResource() != null) {
                    if (theMovieResource.getResource().getResults().size() > 0) {
                        loadData(theMovieResource.getResource().getResults());
                    }
                }else {
                    showErrorMessage();
                }
            }
        });
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


    private void prepareData(List<FavoriteMoveEntity> favoriteMoveEntities) {

        final List<Result> results = new ArrayList<>();
        for (FavoriteMoveEntity favorite : favoriteMoveEntities) {
            results.add(createModelResult(favorite));
        }
        loadData(results);

    }


    private Result createModelResult(FavoriteMoveEntity favorite) {
        Result result = new Result();
        result.setId(favorite.getFavoriteId());
        result.setTitle(favorite.getTitle());
        result.setPosterPath(favorite.getPosterPath());
        result.setBackdropPath(favorite.getBackdropPath());
        result.setReleaseDate(favorite.getDate());
        result.setVoteAverage(favorite.getRate());
        result.setOverview(favorite.getOverview());
        result.setFavorite(favorite.isFavorite());

        return result;
    }

    private void loadData(List<Result> results) {
        if (results.size() > 0) {
            showMovieDataView();
            movieTwoAdapter.setMovieData(results);
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
                currentId = MOST_POPULAR_ID_0;
                break;
            case R.id.action_top_rated:
                currentId = TOP_RATED_ID_1;
                break;
            case R.id.action_my_favorite:
                currentId = MY_FAVORITE_ID_2;
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
        binding.pbLoadingPoster.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.pbLoadingPoster.setVisibility(View.INVISIBLE);
    }


}
