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

package com.example.android.popularmovies.data.db.moviecollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.popularmovies.utilities.JsonUtility;

import java.util.ArrayList;
import java.util.List;

import com.example.android.popularmovies.data.db.moviecollection.MovieCollectionContract.MovieCollectionEntry;

/**
 * @author Julia Mattjus
 */
public class MovieCollectionHelper {

    private static final int INDEX_ID = 0;
    private static final int INDEX_MOVIE_ID = 1;
    private static final int INDEX_MOVIE_COLLECTION_ID = 2;
    private static final int INDEX_NAME = 3;
    private static final int INDEX_POSTER_PATH = 4;
    private static final int INDEX_BACKDROP_PATH = 5;

    private static final String TAG = MovieCollectionHelper.class.getSimpleName();

    /**
     * Method for writing a movie collection to the database
     *
     * @param context
     * @param movieId
     * @param movieCollection
     */
    public static void write(Context context, Integer movieId, com.example.android.popularmovies.data.MovieCollection movieCollection) {

        Log.d(TAG, "write - movieCollection: " + JsonUtility.toJson(movieCollection));

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieCollectionEntry.MOVIE_ID, movieId);
        contentValues.put(MovieCollectionEntry.MOVIE_COLLECTION_ID, movieCollection.getId());
        contentValues.put(MovieCollectionEntry.NAME, getStringOrEmpty(movieCollection.getName()));
        contentValues.put(MovieCollectionEntry.POSTER_PATH, getStringOrEmpty(movieCollection.getPosterPath()));
        contentValues.put(MovieCollectionEntry.BACKDROP_PATH,getStringOrEmpty( movieCollection.getBackdropPath()));

        context.getContentResolver().insert(MovieCollectionEntry.CONTENT_URI, contentValues);
    }

    /**
     * Helper method to make sure we don't have any null values but rather empty strings in case of null
     *
     * @param in
     * @return
     */
    private static String getStringOrEmpty(String in) {
        return in != null ? in : "";
    }

    public static void remove(Context context, Integer movieId) {

        Log.d(TAG, "remove - movieId: " + movieId);

        if(movieId == null) {
            return;
        }

        context.getContentResolver().delete(
                MovieCollectionEntry.CONTENT_URI,
                MovieCollectionEntry.MOVIE_ID + " = ?",
                new String[] { movieId.toString() });
    }

    /**
     * Get a movie collection for a movie by the movie id
     *
     * @param context
     * @param movieId
     * @return
     */
    public static com.example.android.popularmovies.data.MovieCollection getMovieCollectionFromMovieId(Context context, Integer movieId) {

        Log.d(TAG, "getMovieCollectionFromMovieId - movieId: " + movieId);

        if(movieId == null) {
            return null;
        }

        Cursor cursor = context.getContentResolver().query(
                MovieCollectionEntry.CONTENT_URI,
                null,
                MovieCollectionEntry.MOVIE_ID + " = ?",
                new String[] { movieId.toString() },
                null);

        try {
            if(cursor == null || !cursor.moveToFirst()) {
                return null;
            }

            return getMovieCollectionFromCursor(cursor);
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Method for getting a movie collection from a cursor
     *
     * @param cursor
     * @return
     */
    private static com.example.android.popularmovies.data.MovieCollection getMovieCollectionFromCursor(Cursor cursor) {
        Integer id = cursor.getInt(INDEX_MOVIE_COLLECTION_ID);
        String name = cursor.getString(INDEX_NAME);
        String posterPath = cursor.getString(INDEX_POSTER_PATH);
        String backdropPath = cursor.getString(INDEX_BACKDROP_PATH);

        return new com.example.android.popularmovies.data.MovieCollection(id, name, posterPath, backdropPath);
    }
}
