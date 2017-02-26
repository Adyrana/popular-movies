/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieDetailedInfo;
import com.example.android.popularmovies.utilities.ApiKeyUtility;
import com.example.android.popularmovies.utilities.JsonUtility;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Detailed activity for movies
 *
 * @author Julia Mattjus
 */
public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks";

    MovieDetailedInfo mMovie;

    private ConstraintLayout mConstraintLayout;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private FloatingActionButton mFloatingActionButton;

    private TextView mTitleTextView;
    private ImageView mMoviePosterImageView;
    private ImageView mMovieBackdropImageView;
    private TextView mReleaseDateTextView;
    private TextView mRuntimeTextView;
    private TextView mRatingTextView;
    private TextView mSynposisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_detail_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_detail_loading_indicator);

        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mMoviePosterImageView = (ImageView) findViewById(R.id.iv_poster);
        mMovieBackdropImageView = (ImageView) findViewById(R.id.iv_backdrop);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mRuntimeTextView = (TextView) findViewById(R.id.tv_runtime);
        mRatingTextView = (TextView) findViewById(R.id.tv_rating);
        mSynposisTextView = (TextView) findViewById(R.id.tv_synopsis);

        initActionBar();
        setFloatingActionButton(false);

        if(savedInstanceState != null
                && savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_TEXT_KEY)) {
            mMovie = savedInstanceState
                    .getParcelable(LIFECYCLE_CALLBACKS_TEXT_KEY);
            Log.d(TAG, "onCreate mMovie: " + JsonUtility.toJson(mMovie));
            populate(mMovie);
        } else {
            Intent intentThatStartedThisActivity = getIntent();

            if (intentThatStartedThisActivity != null) {
                Integer movieId = intentThatStartedThisActivity.getIntExtra(Intent.EXTRA_UID, 0);

                if(isOnline()) {
                    new FetchMovieTask().execute(movieId);
                } else {
                    showErrorMessage();
                }
            }
        }
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void setFloatingActionButton(boolean on) {
        int star = on ? R.drawable.ic_star_on : R.drawable.ic_star_off;

        mFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(this, star));
    }

    private void setBothInvisible() {
        mConstraintLayout.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showMovieDetailedDataView() {
        mConstraintLayout.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mConstraintLayout.setVisibility(View.INVISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<Integer, Void, MovieDetailedInfo> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setBothInvisible();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieDetailedInfo doInBackground(Integer... params) {
            if (params.length == 0) {
                return null;
            }

            Integer movieId = params[0];
            URL url = NetworkUtils.buildMovieURL(movieId, ApiKeyUtility.readApiKey(getResources()));

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(url);

                Log.d(TAG, "FetchMovieTask - jsonResponse: " + jsonResponse);
                MovieDetailedInfo response = JsonUtility.fromJson(jsonResponse, MovieDetailedInfo.class);

                return response;

            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieDetailedInfo movie) {
            super.onPostExecute(movie);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            populate(movie);
        }
    }

    private void populate(MovieDetailedInfo movie) {
        mMovie = movie;

        if(movie != null) {
            Log.d(TAG, "populate - movie: " + JsonUtility.toJson(movie));

            String posterPath = NetworkUtils.buildImageUrl(movie.getPosterPath(), NetworkUtils.ImageQuality.W185);
            String backdropPath = NetworkUtils.buildImageUrl(movie.getBackdropPath(), NetworkUtils.ImageQuality.W780);
            Log.d(TAG, "populate - movie.getPosterPath(): " + movie.getPosterPath());
            Log.d(TAG, "populate - movie.getBackdropPath(): " + movie.getBackdropPath());
            Log.d(TAG, "populate - posterPath: " + posterPath);
            Log.d(TAG, "populate - backdropPath: " + backdropPath);

            String releaseDate = movie.getReleaseDate();
            int dashPosition = movie.getReleaseDate().indexOf("-");
            if(dashPosition != -1) {
                releaseDate = releaseDate.substring(0, dashPosition);
            }

            mTitleTextView.setText(movie.getTitle());

            /*
             * Copyright 2013 Square, Inc.
             *
             * Licensed under the Apache License, Version 2.0 (the "License");
             * you may not use this file except in compliance with the License.
             * You may obtain a copy of the License at
             *
             *    http://www.apache.org/licenses/LICENSE-2.0
             *
             * Unless required by applicable law or agreed to in writing, software
             * distributed under the License is distributed on an "AS IS" BASIS,
             * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
             * See the License for the specific language governing permissions and
             * limitations under the License.
             */
            Picasso.with(mMoviePosterImageView.getContext()).load(posterPath).into(mMoviePosterImageView);
            Picasso.with(mMovieBackdropImageView.getContext()).load(backdropPath).into(mMovieBackdropImageView);

            mReleaseDateTextView.setText(releaseDate);
            mRuntimeTextView.setText(movie.getRuntime().toString() + getResources().getString(R.string.detail_minutes));
            mRatingTextView.setText(movie.getVoteAverage().toString() + getResources().getString(R.string.detail_of_ten));
            mSynposisTextView.setText(movie.getOverview());

            showMovieDetailedDataView();
        } else {
            showErrorMessage();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState mMovie: " + JsonUtility.toJson(mMovie));
        if(mMovie != null) {
            outState.putParcelable(LIFECYCLE_CALLBACKS_TEXT_KEY, mMovie);
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
