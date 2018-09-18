package com.ligeirostudio.movieone.view.moviedetail;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.ligeirostudio.movieone.Constants;
import com.ligeirostudio.movieone.CustomLoaderCallbacks;
import com.ligeirostudio.movieone.R;
import com.ligeirostudio.movieone.database.AppDatabase;
import com.ligeirostudio.movieone.database.FavoriteMoveEntity;
import com.ligeirostudio.movieone.databinding.ActivityDetailsBinding;
import com.ligeirostudio.movieone.model.movie.Result;
import com.ligeirostudio.movieone.model.review.Review;
import com.ligeirostudio.movieone.model.video.Video;
import com.ligeirostudio.movieone.model.video.VideoResult;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerClickListener,
        CustomLoaderCallbacks.OnTaskLoaderCompleted {

    private ActivityDetailsBinding binding;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private Result movieResult;
    private AppDatabase db;

    private DetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        db = AppDatabase.getInstance(this);
        bindViews();
        setupViewModel();
        setupTrailerAdapter();
        setupReviewAdapter();

        binding.cbFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.cbFavorite.isChecked()){
                    saveMovieToFavorite();
                }else {
                    deleteMovieToFavorite();
                }

            }
        });

    }

    private void setupViewModel() {

        final DetailsViewModelFactory factory = new DetailsViewModelFactory(db, movieResult);

        viewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel.class);

        viewModel.getVideo().observe(this, new Observer<Resource<Video>>() {
            @Override
            public void onChanged(@Nullable Resource<Video> videoResource) {

                if (videoResource.getResource() != null) {
                    if (videoResource.getResource().getResults().size() > 0){
                        loaderCompleted(videoResource.getResource());
                    }else {
                        hideLineLoading();
                    }

                }else {
                    hideLineLoading();
                }
            }
        });

        viewModel.getReview().observe(this, new Observer<Resource<Review>>() {
            @Override
            public void onChanged(@Nullable Resource<Review> reviewResource) {

                if (reviewResource.getResource() != null) {
                    if (reviewResource.getResource().getResults().size() > 0){
                        loaderCompleted(reviewResource.getResource());
                    }

                }
            }
        });


        viewModel.getFavoriteById().observe(this, new Observer<FavoriteMoveEntity>() {
            @Override
            public void onChanged(@Nullable FavoriteMoveEntity favoriteMoveEntity) {
                if (favoriteMoveEntity != null) {
                    if ((favoriteMoveEntity.getFavoriteId() == movieResult.getId())) {
                        binding.cbFavorite.setChecked(true);
                    }
                }
            }
        });


    }

    private void bindViews() {
        if (getIntent() != null) {
            if (getIntent().hasExtra("result")) {
                movieResult = getIntent().getParcelableExtra("result");

                setTitle(movieResult.getTitle());

                Picasso.get()
                        .load(Constants.POSTER_URL + Constants.SIZE_W1280 + movieResult.getPosterPath())
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(binding.imgPoster);
                binding.tvRate.setText(String.valueOf(movieResult.getVoteAverage()));
                binding.tvDate.setText(formatDate(movieResult.getReleaseDate()));
                binding.tvOverview.setText(movieResult.getOverview());
            }
        }
    }


    private void setupTrailerAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerViewVideo.setLayoutManager(layoutManager);
        binding.recyclerViewVideo.setHasFixedSize(true);
        trailerAdapter = new TrailerAdapter(this);
        binding.recyclerViewVideo.setAdapter(trailerAdapter);
    }

    private void setupReviewAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewReview.setLayoutManager(layoutManager);
        binding.recyclerViewReview.setHasFixedSize(true);
        reviewAdapter = new ReviewAdapter();
        binding.recyclerViewReview.setAdapter(reviewAdapter);
    }

    @Override
    public void onTrailerClick(VideoResult result) {
        if (!result.getKey().isEmpty()) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_APP + result.getKey()));

            if (intent.resolveActivity(getPackageManager()) == null) {
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(Constants.YOUTUBE_URL + result.getKey()));
            }

            startActivity(intent);
        }
    }


    private void hideLineLoading() {
        binding.pbLoadingDetails.setVisibility(View.INVISIBLE);
        binding.line.setVisibility(View.VISIBLE);
    }


    @Override
    public void loaderCompleted(Object data) {
        if (data instanceof Video) {
            hideLineLoading();
            Video result = (Video) data;
            trailerAdapter.setData(result.getResults());
            binding.tvTitleTrailer.setVisibility(View.VISIBLE);
        } else if (data instanceof Review) {
            Review review = (Review) data;
            reviewAdapter.setData(review.getResults());
            binding.tvTitleReview.setVisibility(View.VISIBLE);
        }
    }


    private void saveMovieToFavorite() {
        final FavoriteMoveEntity favorite = new FavoriteMoveEntity(
                movieResult.getId(),
                movieResult.getTitle(),
                movieResult.getPosterPath(),
                movieResult.getBackdropPath(),
                movieResult.getVoteAverage(),
                movieResult.getReleaseDate(),
                movieResult.getOverview(),
                true);

        viewModel.saveFavorite(favorite);

    }

    private void deleteMovieToFavorite() {
        viewModel.deleteFavorite();
    }


    private String formatDate(String releaseDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date data = null;
        try {
            data = dateFormat.parse(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateFormat.applyPattern("dd/MM/yyyy");
        return dateFormat.format(data);
    }

}
