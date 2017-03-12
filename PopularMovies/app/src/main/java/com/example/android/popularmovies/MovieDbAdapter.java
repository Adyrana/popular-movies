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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.data.db.movies.MovieDetailedInfosProvider;
import com.example.android.popularmovies.utilities.ImageUtility;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * {@link MovieDbAdapter} exposes a list of movies to a
 * {@link android.support.v7.widget.RecyclerView}
 *
 * @author Julia Mattjus
 */
public class MovieDbAdapter extends RecyclerView.Adapter<MovieDbAdapter.MovieDbAdapterViewHolder> {
    private static final String TAG = MovieDbAdapter.class.getSimpleName();

    private final IMovieAdapterOnClickHandler mClickHandler;

    private final Context mContext;
    private Cursor mCursor;

    /**
     * Creates a MovieDbAdapter
     *
     * @param context      Used to talk to the UI and app resources
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public MovieDbAdapter(@NonNull Context context, IMovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a movie list item.
     */
    public class MovieDbAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mMoviePosterImageView;

        public MovieDbAdapterViewHolder(View view) {
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
            mCursor.moveToPosition(adapterPosition);
            Integer movieId = MovieDetailedInfosProvider.getMovieIdFromMainProjectionCursor(mCursor);
            mClickHandler.onClick(movieId);
        }
    }

    @Override
    public MovieDbAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieDbAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieDbAdapterViewHolder movieDbAdapterViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder - position: " + position);
        mCursor.moveToPosition(position);

        String posterPath = MovieDetailedInfosProvider.getPosterPathFromMainProjectionCursor(mCursor);

        if(ImageUtility.hasLocalImageFile(mContext, posterPath)) {
            Log.d(TAG, "onBindViewHolder - setting saved poster image");
            Picasso.with(movieDbAdapterViewHolder.mMoviePosterImageView.getContext()).load(ImageUtility.getFileFromPath(mContext, posterPath)).into(movieDbAdapterViewHolder.mMoviePosterImageView);
        } else {

            String posterUrl = NetworkUtils.buildImageUrl(posterPath, NetworkUtils.ImageQuality.W342);

            Log.d(TAG, "onBindViewHolder - posterPath: " + posterUrl);

            Picasso.with(movieDbAdapterViewHolder.mMoviePosterImageView.getContext()).load(posterUrl).into(movieDbAdapterViewHolder.mMoviePosterImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public int getPosition() {
        if(mCursor != null) {
            return mCursor.getPosition();
        } else {
            return 0;
        }
    }

    public void setPosition(int position) {
        if(mCursor != null) {
            mCursor.moveToPosition(position);
        }
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
    }
}
