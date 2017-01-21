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
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.TheMovieDbResponse;
import com.example.android.popularmovies.utilities.JsonUtility;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TheMovieDbAdapter.TheMovieDbAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

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

        loadMovieData();
    }

    private void loadMovieData() {
        showMovieDataView();

        new FetchMoviesTask().execute(NetworkUtils.Sorting.POPULAR);
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("Movie", movie);
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
            URL url = NetworkUtils.buildUrl(sorting, readApiKey());

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(url);

                Log.v(TAG, "jsonResponse: " + jsonResponse);
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
                Log.v(TAG, "movies: " + JsonUtility.toJson(movies));
                mTheMovieDbAdapter.setMMovies(movies);
            } else {
                showErrorMessage();
            }
        }
    }

    private String readApiKey() {
        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.the_movie_db);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            return new String(b);
        } catch (Exception e) {
            Log.e(TAG, "Unable to read API file the_movie_db.txt", e);
            return "";
        }
    }
}
