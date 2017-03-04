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
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieDetailedInfo;
import com.example.android.popularmovies.data.db.movies.MovieDetailedInfosProvider;
import com.example.android.popularmovies.utilities.ApiKeyUtility;
import com.example.android.popularmovies.utilities.JsonUtility;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Detailed activity for movies
 *
 * @author Julia Mattjus
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetailedInfo> {

    @Override
    public Loader<MovieDetailedInfo> onCreateLoader(int loaderId, Bundle args) {
        final Context context = this;

        mCurrentLoader = loaderId;

        switch (loaderId) {
            case FAVOURITE_LOADER_ID:
                return new AsyncTaskLoader<MovieDetailedInfo>(context) {

                    MovieDetailedInfo mTaskData = null;

                    @Override
                    protected void onStartLoading () {
                        setBothInvisible();
                        mLoadingIndicator.setVisibility(View.VISIBLE);

                        if (mTaskData != null) {
                            deliverResult(mTaskData);
                        } else {
                            forceLoad();
                        }
                    }

                    @Override
                    public MovieDetailedInfo loadInBackground() {
                        return MovieDetailedInfosProvider.getFavourite(context, mMovieId);
                    }

                    public void deliverResult(MovieDetailedInfo data) {
                        mTaskData = data;
                        super.deliverResult(data);
                    }
                };
            case THE_MOVIE_DB_LOADER_ID:
                return new AsyncTaskLoader<MovieDetailedInfo>(context) {

                    MovieDetailedInfo mTaskData = null;

                    @Override
                    protected void onStartLoading () {
                        if (mTaskData != null) {
                            deliverResult(mTaskData);
                        } else {
                            forceLoad();
                        }
                    }

                    @Override
                    public MovieDetailedInfo loadInBackground() {
                        URL url = NetworkUtils.buildMovieURL(mMovieId, ApiKeyUtility.readApiKey(getResources()));

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

                    public void deliverResult(MovieDetailedInfo data) {
                        mTaskData = data;
                        super.deliverResult(data);
                    }
                };
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<MovieDetailedInfo> loader, MovieDetailedInfo data) {
        switch (loader.getId()) {
            case FAVOURITE_LOADER_ID:
                if(data == null) {
                    if(isOnline()) {
                        getSupportLoaderManager().initLoader(THE_MOVIE_DB_LOADER_ID, null, this);
                    } else {
                        mCurrentLoader = FAVOURITE_LOADER_ID;
                        showErrorMessage();
                    }
                } else {
                    setFloatingActionButton(true);
                    mMovie = data;
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    populate(data);
                    mCurrentLoader = FAVOURITE_LOADER_ID;
                }
                break;
            case THE_MOVIE_DB_LOADER_ID:
                mMovie = data;
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                populate(data);
                mCurrentLoader = FAVOURITE_LOADER_ID;
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieDetailedInfo> loader) {
        mMovie = null;
        mMovieId = null;
        mCurrentLoader = FAVOURITE_LOADER_ID;
    }

    private MovieDetailedInfo mMovie;
    private Boolean mIsFavourite = false;
    private Integer mMovieId;
    private int mCurrentLoader = FAVOURITE_LOADER_ID;

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks";

    private static final int FAVOURITE_LOADER_ID = 1;
    private static final int THE_MOVIE_DB_LOADER_ID = 2;

    @BindView(R.id.constraint_layout) ConstraintLayout mConstraintLayout;
    @BindView(R.id.tv_detail_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_detail_loading_indicator) ProgressBar mLoadingIndicator;
    @BindView(R.id.floatingActionButton) FloatingActionButton mFloatingActionButton;

    @BindView(R.id.tv_title) TextView mTitleTextView;
    @BindView(R.id.iv_poster) ImageView mMoviePosterImageView;
    @BindView(R.id.iv_backdrop) ImageView mMovieBackdropImageView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_runtime) TextView mRuntimeTextView;
    @BindView(R.id.tv_rating) TextView mRatingTextView;
    @BindView(R.id.tv_synopsis) TextView mSynposisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        initActionBar();

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mIsFavourite) {
                    setFloatingActionButton(false);
                    MovieDetailedInfosProvider.removeFavourite(v.getContext(), mMovie);
                } else {
                    setFloatingActionButton(true);
                    MovieDetailedInfosProvider.write(v.getContext(), mMovie);
                }
            }
        });

        if(savedInstanceState != null && savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_TEXT_KEY)) {
            mMovie = savedInstanceState.getParcelable(LIFECYCLE_CALLBACKS_TEXT_KEY);
            mMovieId = mMovie.getId();
            Log.d(TAG, "onCreate mMovie: " + JsonUtility.toJson(mMovie));
            populate(mMovie);
        } else {
            Log.d(TAG, "onCreate Movie data needs to be loaded");
            Intent intentThatStartedThisActivity = getIntent();

            if (intentThatStartedThisActivity != null) {
                mMovieId = intentThatStartedThisActivity.getIntExtra(Intent.EXTRA_UID, 0);
                Log.d(TAG, "onCreate movieId: " + mMovieId);

                getSupportLoaderManager().initLoader(FAVOURITE_LOADER_ID, null, this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportLoaderManager().restartLoader(mCurrentLoader, null, this);
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
        mIsFavourite = on;

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
