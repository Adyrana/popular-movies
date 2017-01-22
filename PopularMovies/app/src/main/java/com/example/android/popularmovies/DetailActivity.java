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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.MovieDetailedInfo;
import com.example.android.popularmovies.utilities.ApiKeyUtility;
import com.example.android.popularmovies.utilities.JsonUtility;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

/**
 * Detailed activity for movies
 *
 * @author Julia Mattjus
 */
public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks";

    MovieDetailedInfo mMovie;

    private LinearLayout mDetailedMovieLinearLayout;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private TextView mTitleTextView;
    private ImageView mMoviePosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mRuntimeTextView;
    private TextView mRatingTextView;
    private TextView mSynposisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDetailedMovieLinearLayout = (LinearLayout) findViewById(R.id.ll_detailed_movie);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_detail_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_detail_loading_indicator);

        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mMoviePosterImageView = (ImageView) findViewById(R.id.iv_poster);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mRuntimeTextView = (TextView) findViewById(R.id.tv_runtime);
        mRatingTextView = (TextView) findViewById(R.id.tv_rating);
        mSynposisTextView = (TextView) findViewById(R.id.tv_synopsis);

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

                new FetchMovieTask().execute(movieId);
            }
        }
    }

    private void showMovieDetailedDataView() {
        mDetailedMovieLinearLayout.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mDetailedMovieLinearLayout.setVisibility(View.INVISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<Integer, Void, MovieDetailedInfo> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            String posterPath = NetworkUtils.buildPosterUrl(movie.getPosterPath());
            Log.d(TAG, "populate - movie.getPosterPath(): " + movie.getPosterPath());
            Log.d(TAG, "populate - posterPath: " + posterPath);

            String releaseDate = movie.getReleaseDate();
            int dashPosition = movie.getReleaseDate().indexOf("-");
            if(dashPosition != -1) {
                releaseDate = releaseDate.substring(0, dashPosition);
            }

            mTitleTextView.setText(movie.getTitle());
            Picasso.with(mMoviePosterImageView.getContext()).load(posterPath).into(mMoviePosterImageView);
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
}
