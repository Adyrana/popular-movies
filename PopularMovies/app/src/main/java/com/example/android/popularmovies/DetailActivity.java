/*
 * Copyright (C) 2017 Julia Mattjus
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
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieDetailedInfo;
import com.example.android.popularmovies.data.Video;
import com.example.android.popularmovies.data.db.movies.MovieDetailedInfosHelper;
import com.example.android.popularmovies.utilities.ApiKeyUtility;
import com.example.android.popularmovies.utilities.ImageUtility;
import com.example.android.popularmovies.utilities.JsonUtility;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.AllArgsConstructor;

/**
 * Detailed activity for movies
 *
 * @author Julia Mattjus
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetailedInfo>, VideoAdapter.IVideoOnClickHandler {

    @AllArgsConstructor
    private class DataWriter extends AsyncTask {

        private Context context;
        private MovieDetailedInfo movie;

        @Override
        protected Object doInBackground(Object[] params) {
            if(movie != null) {
                MovieDetailedInfosHelper.write(context, movie);
            }
            return null;
        }
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

    @BindView(R.id.tv_review_header) TextView mReviewsTextView;
    @BindView(R.id.recyclerview_reviews) RecyclerView mRecyclerViewReviews;
    @BindView(R.id.tv_video_header) TextView mVideosTextView;
    @BindView(R.id.recyclerview_videos) RecyclerView mRecyclerViewVideos;

    @BindView(R.id.tv_title) TextView mTitleTextView;
    @BindView(R.id.iv_poster) ImageView mMoviePosterImageView;
    @BindView(R.id.iv_backdrop) ImageView mMovieBackdropImageView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_runtime) TextView mRuntimeTextView;
    @BindView(R.id.tv_rating) TextView mRatingTextView;
    @BindView(R.id.iv_share_movie_button) ImageView mShareImageView;
    @BindView(R.id.tv_synopsis) TextView mSynposisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        initActionBar();

        mMoviePosterImageView.setImageDrawable(null);
        mMovieBackdropImageView.setImageDrawable(null);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mIsFavourite) {
                    setFloatingActionButton(false);
                    MovieDetailedInfosHelper.removeFavourite(v.getContext(), mMovie);
                    ImageUtility.removeImage(v.getContext(), mMovie.getPosterPath());
                    ImageUtility.removeImage(v.getContext(), mMovie.getBackdropPath());
                } else {
                    setFloatingActionButton(true);
                    if(mMovie != null) {
                        MovieDetailedInfosHelper.write(v.getContext(), mMovie);
                        ImageUtility.saveImage(v.getContext(), ((BitmapDrawable) mMoviePosterImageView.getDrawable()).getBitmap(), mMovie.getPosterPath());
                        ImageUtility.saveImage(v.getContext(), ((BitmapDrawable) mMovieBackdropImageView.getDrawable()).getBitmap(), mMovie.getBackdropPath());
                    }
                }
            }
        });

        mShareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMovie != null && mMovie.getId() != null) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/html");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://www.themoviedb.org/movie/" + mMovie.getId());
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));
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

    @Override
    public Loader<MovieDetailedInfo> onCreateLoader(int loaderId, final Bundle args) {
        final Context context = this;

        mCurrentLoader = loaderId;

        switch (loaderId) {
            case FAVOURITE_LOADER_ID:
                Log.d(TAG, "Starting favourite async task loader for movieId: " + mMovieId);
                return new AsyncTaskLoader<MovieDetailedInfo>(context) {

                    MovieDetailedInfo mTaskData = null;

                    @Override
                    protected void onStartLoading () {
                        setInvisible();
                        mLoadingIndicator.setVisibility(View.VISIBLE);

                        if (mTaskData != null) {
                            deliverResult(mTaskData);
                        } else {
                            forceLoad();
                        }
                    }

                    @Override
                    public MovieDetailedInfo loadInBackground() {
                        return MovieDetailedInfosHelper.getFavourite(context, mMovieId);
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
                            final MovieDetailedInfo response = JsonUtility.fromJson(jsonResponse, MovieDetailedInfo.class);

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

    private void showReviews() {
        mReviewsTextView.setVisibility(View.VISIBLE);
        mRecyclerViewReviews.setVisibility(View.VISIBLE);
    }

    private void showVideos() {
        mVideosTextView.setVisibility(View.VISIBLE);
        mRecyclerViewVideos.setVisibility(View.VISIBLE);
    }

    private void setInvisible() {
        mConstraintLayout.setVisibility(View.INVISIBLE);
        mFloatingActionButton.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mReviewsTextView.setVisibility(View.INVISIBLE);
        mRecyclerViewReviews.setVisibility(View.INVISIBLE);
        mVideosTextView.setVisibility(View.INVISIBLE);
        mRecyclerViewVideos.setVisibility(View.INVISIBLE);
    }

    private void showMovieDetailedDataView() {
        mConstraintLayout.setVisibility(View.VISIBLE);
        mFloatingActionButton.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mFloatingActionButton.setVisibility(View.INVISIBLE);
        mConstraintLayout.setVisibility(View.INVISIBLE);
    }

    private void populate(final MovieDetailedInfo movie) {
        mMovie = movie;

        if(movie != null) {
            Log.d(TAG, "populate - movie: " + JsonUtility.toJson(movie));

            String releaseDate = movie.getReleaseDate();
            int dashPosition = movie.getReleaseDate().indexOf("-");
            if(dashPosition != -1) {
                releaseDate = releaseDate.substring(0, dashPosition);
            }

            mTitleTextView.setText(movie.getTitle());

            if(ImageUtility.hasLocalImageFile(this, movie.getPosterPath())) {
                Log.d(TAG, "populate - setting saved poster image");
                Picasso.with(mMoviePosterImageView.getContext()).load(ImageUtility.getFileFromPath(this, movie.getPosterPath())).into(mMoviePosterImageView);
            } else {
                String posterPath = NetworkUtils.buildImageUrl(movie.getPosterPath(), NetworkUtils.ImageQuality.W342);
                Log.d(TAG, "populate - movie.getPosterPath(): " + movie.getPosterPath());
                Log.d(TAG, "populate - posterPath: " + posterPath);
                Picasso.with(mMoviePosterImageView.getContext()).load(posterPath).into(mMoviePosterImageView);
            }

            if(ImageUtility.hasLocalImageFile(this, movie.getBackdropPath())) {
                Log.d(TAG, "populate - setting saved backdrop image");
                Picasso.with(mMovieBackdropImageView.getContext()).load(ImageUtility.getFileFromPath(this, movie.getBackdropPath())).into(mMovieBackdropImageView);
            } else {
                String backdropPath = NetworkUtils.buildImageUrl(movie.getBackdropPath(), NetworkUtils.ImageQuality.W780);
                Log.d(TAG, "populate - movie.getBackdropPath(): " + movie.getBackdropPath());
                Log.d(TAG, "populate - backdropPath: " + backdropPath);
                Picasso.with(mMovieBackdropImageView.getContext()).load(backdropPath).into(mMovieBackdropImageView);
            }

            mReleaseDateTextView.setText(releaseDate);
            mRuntimeTextView.setText(movie.getRuntime().toString() + getResources().getString(R.string.detail_minutes));
            mRatingTextView.setText(movie.getVoteAverage().toString() + getResources().getString(R.string.detail_of_ten));

            mSynposisTextView.setText(movie.getOverview());

            showMovieDetailedDataView();
            if(movie.getReviews().getResults().size() > 0) {
                Log.d(TAG, "populate - reviewAuthors: " + JsonUtility.toJson(movie.getReviews().getResults()));
                mRecyclerViewReviews.setHasFixedSize(true);
                mRecyclerViewReviews.setNestedScrollingEnabled(false);
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
                mRecyclerViewReviews.setLayoutManager(linearLayoutManager1);
                ReviewAdapter reviewAdapter = new ReviewAdapter(movie.getReviews().getResults());
                mRecyclerViewReviews.setAdapter(reviewAdapter);
                showReviews();
            }
            if(movie.getVideos().getResults().size() > 0) {
                Log.d(TAG, "populate - videos: " + JsonUtility.toJson(movie.getVideos().getResults()));
                mRecyclerViewVideos.setHasFixedSize(true);
                mRecyclerViewVideos.setNestedScrollingEnabled(false);
                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
                mRecyclerViewVideos.setLayoutManager(linearLayoutManager2);
                DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(mRecyclerViewVideos.getContext(), linearLayoutManager2.getOrientation());
                mRecyclerViewVideos.addItemDecoration(dividerItemDecoration2);
                VideoAdapter videoAdapter = new VideoAdapter(getYoutubeVideos(movie.getVideos().getResults()), this);
                mRecyclerViewVideos.setAdapter(videoAdapter);
                showVideos();
            }
        } else {
            showErrorMessage();
        }
    }

    private List<Video> getYoutubeVideos(List<Video> videos) {
        List<Video> youtubeVideos = new ArrayList<>();
        for(Video video : videos) {
            if(video.getSite().equalsIgnoreCase("youtube")) {
                youtubeVideos.add(video);
            }
        }
        return youtubeVideos;
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

    @Override
    public void onClick(Video video, VideoAdapter.CLICK_TYPE clickType) {
        switch(clickType) {
            case PLAY:
                if(video.getKey() != null && !video.getKey().isEmpty()) {
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));

                    if(intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                break;
            case SHARE:
                if(video.getKey() != null && !video.getKey().isEmpty()) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/html");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + video.getKey());

                    if(sharingIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.action_share)));
                    }
                }
                break;
            default:
                break;
        }
    }
}
