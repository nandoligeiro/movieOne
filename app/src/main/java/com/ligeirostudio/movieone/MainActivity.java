package com.ligeirostudio.movieone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ligeirostudio.movieone.model.Result;
import com.ligeirostudio.movieone.model.TheMovie;
import com.ligeirostudio.movieone.retrofit.RequesterApi;



public class MainActivity extends AppCompatActivity implements MovieOneAdapter.ListItemClickListener,
        FetchDataMovieTask.OnTaskCompleted {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private MovieOneAdapter movieOneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.pb_loading_poster);
        errorTextView = findViewById(R.id.tv_error_message_display);
        setupAdapter();
        loadMostPopularMovies();
    }

    private void loadMostPopularMovies() {
        showLoading();
        new FetchDataMovieTask(this).execute(new RequesterApi().getApi().getMostPopular());
    }

    private void loadTopRatedMovies() {
        showLoading();
        new FetchDataMovieTask(this).execute(new RequesterApi().getApi().getTopRated());
    }

    private void setupAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        movieOneAdapter = new MovieOneAdapter(this);
        recyclerView.setAdapter(movieOneAdapter);
    }


    @Override
    public void onListItemClick(Result result) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("result", result);
        startActivity(intent);
    }

    @Override
    public void onTaskCompleted(TheMovie movie) {
        if (movie != null) {
            showMovieDataView();
            movieOneAdapter.setMovieData(movie);
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

        movieOneAdapter.setMovieData(null);

        switch (item.getItemId()) {
            case R.id.action_popular:
                loadMostPopularMovies();
                break;
            case R.id.action_top_rated:
                loadTopRatedMovies();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMovieDataView() {
        hideLoading();
        errorTextView.setVisibility(View.INVISIBLE);
    }


    private void showErrorMessage() {
        hideLoading();
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

}
