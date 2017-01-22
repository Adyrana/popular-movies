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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link TheMovieDbAdapter} exposes a list of movies to a
 * {@link android.support.v7.widget.RecyclerView}
 *
 * @author Julia Mattjus
 */
public class TheMovieDbAdapter extends RecyclerView.Adapter<TheMovieDbAdapter.TheMovieDbAdapterViewHolder> {

    private static final String TAG = TheMovieDbAdapter.class.getSimpleName();

    private List<Movie> mMovies;

    private final TheMovieDbAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface TheMovieDbAdapterOnClickHandler {
        void onClick(Movie movieData);
    }

    /**
     * Creates a TheMovieDbAdapter
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public TheMovieDbAdapter(TheMovieDbAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a movie list item.
     */
    public class TheMovieDbAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMoviePosterImageView;

        public TheMovieDbAdapterViewHolder(View view) {
            super(view);
            mMoviePosterImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

    @Override
    public TheMovieDbAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TheMovieDbAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TheMovieDbAdapterViewHolder theMovieDbAdapterViewHolder, int position) {
        Movie movie = mMovies.get(position);
        String posterPath = NetworkUtils.buildPosterUrl(movie.getPosterPath());
        Log.d(TAG, "movie.getPosterPath(): " + movie.getPosterPath());
        Log.d(TAG, "posterPath: " + posterPath);

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
        Picasso.with(theMovieDbAdapterViewHolder.mMoviePosterImageView.getContext()).load(posterPath).into(theMovieDbAdapterViewHolder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovies) return 0;
        return mMovies.size();
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    /**
     * Set the movies for a TheMovieDbAdapter
     *
     * @param movies
     */
    public void setMovies(List<Movie> movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }
}
