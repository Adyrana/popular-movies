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

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.TheMovieDbResponse;
import com.example.android.popularmovies.utilities.ApiKeyUtility;
import com.example.android.popularmovies.utilities.JsonUtility;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The main activity which shows an grid view of movies.
 *
 * @author Julia Mattjus
 */
public class MainActivity extends AppCompatActivity implements TheMovieDbAdapter.TheMovieDbAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks";

    private RecyclerView mRecyclerView;
    private TheMovieDbAdapter mTheMovieDbAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mTheMovieDbAdapter = new TheMovieDbAdapter(this);
        mRecyclerView.setAdapter(mTheMovieDbAdapter);

        if(savedInstanceState != null
                && savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_TEXT_KEY)) {
                ArrayList<Movie> movies = savedInstanceState
                        .getParcelableArrayList(LIFECYCLE_CALLBACKS_TEXT_KEY);
                Log.d(TAG, "onCreate movies: " + JsonUtility.toJson(movies));
                showMovieDataView();
                mTheMovieDbAdapter.setMovies(movies);
        } else {
            loadMovieData(NetworkUtils.Sorting.POPULAR);
        }
    }

    /**
     * This method will start a new task that fetches movies according to the specified sorting.
     *
     * @param sorting The sorting to use when requesting movies from THe Movie DB
     */
    private void loadMovieData(NetworkUtils.Sorting sorting) {
        showMovieDataView();

        new FetchMoviesTask().execute(sorting);
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param movie The movie that was clicked
     */
    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_UID, movie.getId());
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

    public class FetchMoviesTask extends AsyncTask<NetworkUtils.Sorting, Void, List<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(NetworkUtils.Sorting... params) {
            if (params.length == 0) {
                return null;
            }

            NetworkUtils.Sorting sorting = params[0];
            URL url = NetworkUtils.buildUrl(sorting, ApiKeyUtility.readApiKey(getResources()));

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(url);

                Log.d(TAG, "FetchMoviesTask - jsonResponse: " + jsonResponse);
                TheMovieDbResponse response = JsonUtility.fromJson(jsonResponse, TheMovieDbResponse.class);

                return response.getResults();

            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if(movies != null) {
                showMovieDataView();
                Log.d(TAG, "onPostExecute movies: " + JsonUtility.toJson(movies));
                mTheMovieDbAdapter.setMovies(movies);
            } else {
                showErrorMessage();
            }
        }
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
            loadMovieData(NetworkUtils.Sorting.POPULAR);
            return true;
        } else if (id == R.id.action_top_rated) {
            loadMovieData(NetworkUtils.Sorting.TOP_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Movie> movies = (ArrayList<Movie>) mTheMovieDbAdapter.getMMovies();
        Log.d(TAG, "onSaveInstanceState movies: " + JsonUtility.toJson(movies));
        if(movies != null) {
            outState.putParcelableArrayList(LIFECYCLE_CALLBACKS_TEXT_KEY, movies);
        }
    }
}
