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
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.MovieDetailedInfo;
import com.example.android.popularmovies.data.TheMovieDbResponse;
import com.example.android.popularmovies.data.db.movies.MovieDetailedInfosProvider;
import com.example.android.popularmovies.utilities.ApiKeyUtility;
import com.example.android.popularmovies.utilities.JsonUtility;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The main activity which shows an grid view of movies.
 *
 * @author Julia Mattjus
 */
public class MainActivity extends AppCompatActivity implements IMovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<RecyclerView.Adapter> {

    private enum MoviesSource {
        DATABASE, THE_MOVIE_DB
    }

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LIFECYCLE_CALLBACKS_MOVIES_KEY = "movies";
    private static final String LIFECYCLE_CALLBACKS_SOURCE_KEY = "source";
    private static final String LIFECYCLE_CALLBACKS_CURSOR_POSITION_KEY = "cursor_position";

    private static final int THE_MOVIE_DB_LOADER_ID = 1;
    private static final int DATABASE_LOADER_ID = 2;

    @BindView(R.id.recyclerview_movies) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    private TheMovieDbAdapter mTheMovieDbAdapter;
    private MovieDbAdapter mMovieDbAdapter;
    private int mPosition = RecyclerView.NO_POSITION;
    private MoviesSource mSource;
    private NetworkUtils.Sorting mSorting;
    private int mCurrentLoader = THE_MOVIE_DB_LOADER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        final int columns = getResources().getInteger(R.integer.grid_columns);
        GridLayoutManager layoutManager = new GridLayoutManager(this, columns);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mTheMovieDbAdapter = new TheMovieDbAdapter(this);
        mMovieDbAdapter = new MovieDbAdapter(this, this);
        mRecyclerView.setAdapter(mTheMovieDbAdapter);

        mSorting = NetworkUtils.Sorting.POPULAR; // Default sorting
        if(isOnline()) {
            mSource = MoviesSource.THE_MOVIE_DB; // Default source
        } else {
            mSource = MoviesSource.DATABASE; // Default source
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_SOURCE_KEY)) {

            Log.d(TAG, "There is an saved state");
            // If the source is the database it'll be read from database using the loadMovieData call.
            mSource = MoviesSource.valueOf(savedInstanceState.getString(LIFECYCLE_CALLBACKS_SOURCE_KEY));
            Log.d(TAG, "saved mSource: " + mSource);

            if(mSource == MoviesSource.THE_MOVIE_DB) {
                ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(LIFECYCLE_CALLBACKS_MOVIES_KEY);
                Log.d(TAG, "onCreate movies: " + JsonUtility.toJson(movies));
                showMovieDataView();
                mTheMovieDbAdapter.setMovies(movies);
            } else if(mSource == MoviesSource.DATABASE) {
                mPosition = savedInstanceState.getInt(LIFECYCLE_CALLBACKS_CURSOR_POSITION_KEY);
                loadMovieData();
            } else {
                loadMovieData();
            }
        } else {
            loadMovieData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSource != null && mSource == MoviesSource.DATABASE && mMovieDbAdapter != null) {
            mPosition = mMovieDbAdapter.getPosition();
            loadMovieData();
        }
    }

    /**
     * This method will start a new task that fetches movies according to the specified sorting.
     *
     */
    private void loadMovieData() {
        showMovieDataView();

        if(mSource == MoviesSource.THE_MOVIE_DB) {
            if (isOnline()) {
                getSupportLoaderManager().restartLoader(THE_MOVIE_DB_LOADER_ID, null, this);
            } else {
                showErrorMessage();
            }
        } else {
            getSupportLoaderManager().restartLoader(DATABASE_LOADER_ID, null, this);
        }
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param movieId Id of the movie that was clicked
     */
    @Override
    public void onClick(Integer movieId) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_UID, movieId);
        startActivity(intentToStartDetailActivity);
    }

    private void showMovieDataView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }
    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<RecyclerView.Adapter> onCreateLoader(int loaderId, Bundle bundle) {

        final Context context = this;
        mCurrentLoader = loaderId;

        mLoadingIndicator.setVisibility(View.VISIBLE);

        switch (loaderId) {
            case THE_MOVIE_DB_LOADER_ID:
                return new AsyncTaskLoader<RecyclerView.Adapter>(context) {

                    RecyclerView.Adapter mTaskData = null;

                    @Override
                    protected void onStartLoading () {
                        if (mTaskData != null) {
                            Log.d(TAG, "Deliver existing data");
                            deliverResult(mTaskData);
                        } else {
                            Log.d(TAG, "Load new data from The Movie DB");
                            forceLoad();
                        }
                    }

                    @Override
                    public RecyclerView.Adapter loadInBackground() {
                        if (mSorting == null) {
                            return null;
                        }

                        URL url = NetworkUtils.buildUrl(mSorting, ApiKeyUtility.readApiKey(getResources()));

                        try {
                            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);

                            Log.d(TAG, "sorting: " + mSorting.toString());
                            Log.d(TAG, "jsonResponse: " + jsonResponse);
                            TheMovieDbResponse response = JsonUtility.fromJson(jsonResponse, TheMovieDbResponse.class);
                            List<Movie> movies  = response.getResults();

                            if(movies != null) {
                                Log.d(TAG, "movies: " + JsonUtility.toJson(movies));
                                mTheMovieDbAdapter.setMovies(movies);
                            } else {
                                showErrorMessage();
                            }

                            return mTheMovieDbAdapter;

                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                            return null;
                        }
                    }

                    public void deliverResult(RecyclerView.Adapter data) {
                        mTaskData = data;
                        super.deliverResult(data);
                    }
                };
            case DATABASE_LOADER_ID:
                return new AsyncTaskLoader<RecyclerView.Adapter>(context) {

                    RecyclerView.Adapter mTaskData = null;

                    @Override
                    protected void onStartLoading () {
                        if (mTaskData != null) {
                            deliverResult(mTaskData);
                        } else {
                            forceLoad();
                        }
                    }

                    @Override
                    public RecyclerView.Adapter loadInBackground() {
                        Cursor cursor = MovieDetailedInfosProvider.getFavouritesCursor(context);

                        mMovieDbAdapter.swapCursor(cursor);

                        return mMovieDbAdapter;
                    }

                    public void deliverResult(RecyclerView.Adapter data) {
                        mTaskData = data;
                        super.deliverResult(data);
                    }
                };
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<RecyclerView.Adapter> loader, RecyclerView.Adapter data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(data == null) {
            return;
        }

        mRecyclerView.setAdapter(data);

        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.getLayoutManager().scrollToPosition(mPosition);
        Log.d(TAG, "data.getItemCount(): " + data.getItemCount());
        if(data.getItemCount() != 0) showMovieDataView();
    }

    @Override
    public void onLoaderReset(Loader<RecyclerView.Adapter> loader) {
        mMovieDbAdapter.swapCursor(null);
        mTheMovieDbAdapter.setMovies(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            mSource = MoviesSource.THE_MOVIE_DB;
            mSorting = NetworkUtils.Sorting.POPULAR;
            loadMovieData();
            return true;
        } else if (id == R.id.action_top_rated) {
            mSource = MoviesSource.THE_MOVIE_DB;
            mSorting = NetworkUtils.Sorting.TOP_RATED;
            loadMovieData();
            return true;
        } else if (id == R.id.action_favourites) {
            mSource = MoviesSource.DATABASE;
            loadMovieData();
            return true;
        } else if (id == R.id.action_about) {
            final String appName = getResources().getString(R.string.app_name);
            new LibsBuilder().withAboutAppName(appName).withLicenseShown(true).withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR).start(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(LIFECYCLE_CALLBACKS_SOURCE_KEY, mSource.toString());

        if(mSource == MoviesSource.THE_MOVIE_DB) {
            ArrayList<Movie> movies = (ArrayList<Movie>) mTheMovieDbAdapter.getMovies();
            Log.d(TAG, "onSaveInstanceState movies: " + JsonUtility.toJson(movies));
            if (movies != null) {
                outState.putParcelableArrayList(LIFECYCLE_CALLBACKS_MOVIES_KEY, movies);
            }
        } else if(mSource == MoviesSource.DATABASE) {
            outState.putInt(LIFECYCLE_CALLBACKS_CURSOR_POSITION_KEY, mMovieDbAdapter.getPosition());
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
