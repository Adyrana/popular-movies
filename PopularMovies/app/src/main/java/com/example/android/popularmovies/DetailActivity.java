/*
 * Copyright 2017 Julia Mattjus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by Julia on 2017-01-20.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private TextView mTitleTextView;
    private ImageView mMoviePosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mRatingTextView;
    private TextView mSynposisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mMoviePosterImageView = (ImageView) findViewById(R.id.iv_poster);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mRatingTextView = (TextView) findViewById(R.id.tv_rating);
        mSynposisTextView = (TextView) findViewById(R.id.tv_synopsis);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Movie movie = (Movie) intentThatStartedThisActivity.getSerializableExtra("Movie");
            String posterPath = NetworkUtils.buildPosterUrl(movie.getPosterPath());
            Log.v(TAG, "movie.getPosterPath(): " + movie.getPosterPath());
            Log.v(TAG, "posterPath: " + posterPath);

            String releaseDate = movie.getReleaseDate();
            int dashPosition = movie.getReleaseDate().indexOf("-");
            if(dashPosition != -1) {
                releaseDate = releaseDate.substring(0, dashPosition);
            }


            mTitleTextView.setText(movie.getTitle());
            Picasso.with(mMoviePosterImageView.getContext()).load(posterPath).into(mMoviePosterImageView);
            mReleaseDateTextView.setText(releaseDate);
            mRatingTextView.setText(movie.getVoteAverage().toString() + "/10");
            mSynposisTextView.setText(movie.getOverview());
        }
    }
}
