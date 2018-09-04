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
import android.widget.CompoundButton;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.ligeirostudio.movieone.Constants;
import com.ligeirostudio.movieone.CustomLoaderCallbacks;
import com.ligeirostudio.movieone.R;
import com.ligeirostudio.movieone.database.AppDatabase;
import com.ligeirostudio.movieone.database.FavoriteMoveEntity;
import com.ligeirostudio.movieone.databinding.ActivityDetailsBinding;
import com.ligeirostudio.movieone.executor.AppExecutors;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        db = AppDatabase.getInstance(this);
        bindViews();
        setupViewModel();
        checkIsFavorite();

        binding.cbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    saveMovieToFavorite();
                } else {
                    deleteMovieToFavorite();
                }

                binding.cbFavorite.setChecked(isChecked);

            }
        });

    }

    private void setupViewModel() {

        DetailsViewModelFactory factory = new DetailsViewModelFactory(db, movieResult.getId());

        DetailsViewModel viewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel.class);

        viewModel.getVideo().observe(this, new Observer<Resource<Video>>() {
            @Override
            public void onChanged(@Nullable Resource<Video> videoResource) {
                setupTrailerAdapter();
                if (videoResource != null) {
                    loaderCompleted(videoResource.getResource());
                }
            }
        });

        viewModel.getReview().observe(this, new Observer<Resource<Review>>() {
            @Override
            public void onChanged(@Nullable Resource<Review> reviewResource) {
                setupReviewAdapter();
                if (reviewResource != null) {
                    loaderCompleted(reviewResource.getResource());
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
                        .load(Constants.POSTER_URL + Constants.SIZE_W1280 + movieResult.getBackdropPath())
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
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_URL + result.getKey())));
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
        } else if (data instanceof Review) {
            Review review = (Review) data;
            reviewAdapter.setData(review.getResults());
        }
    }

    private void checkIsFavorite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                if (db.favoriteDAO().getByFavoriteId(movieResult.getId()) != null) {
                    if ((db.favoriteDAO().getByFavoriteId(movieResult.getId()).getFavoriteId() == movieResult.getId())) {
                        binding.cbFavorite.setChecked(true);
                    }
                }
            }
        });

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
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (db.favoriteDAO().getByFavoriteId(movieResult.getId()) == null) {
                    db.favoriteDAO().insertFavorite(favorite);

                }
            }
        });

    }

    private void deleteMovieToFavorite() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.favoriteDAO().deleteByFavoriteId(movieResult.getId());
            }
        });

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
