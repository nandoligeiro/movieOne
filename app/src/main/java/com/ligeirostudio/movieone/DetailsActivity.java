package com.ligeirostudio.movieone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.ligeirostudio.movieone.model.Result;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private ImageView posterImageView;
    private TextView title, overview, date, rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        posterImageView = findViewById(R.id.img_poster);
        title = findViewById(R.id.tv_title);
        rate = findViewById(R.id.tv_rate);
        date = findViewById(R.id.tv_date);
        overview = findViewById(R.id.tv_overview);

        bindViews();

    }

    private void bindViews() {

        if (getIntent() != null) {
            if (getIntent().hasExtra("result")) {
                Result movieResult = getIntent().getParcelableExtra("result");
                Picasso.get()
                        .load(Constants.POSTER_URL + Constants.SIZE_W1280 + movieResult.getBackdropPath())
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(posterImageView);

                title.setText(movieResult.getOriginalTitle());
                rate.setText(String.valueOf(movieResult.getVoteAverage()));
                date.setText(formatDate(movieResult.getReleaseDate()));
                overview.setText(movieResult.getOverview());
            }
        }
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
