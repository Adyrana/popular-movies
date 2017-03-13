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

package com.example.android.popularmovies.data.db.genres;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.popularmovies.data.Genre;
import com.example.android.popularmovies.utilities.JsonUtility;

import java.util.ArrayList;
import java.util.List;

import com.example.android.popularmovies.data.db.genres.GenresContract.GenresEntry;

/**
 * @author Julia Mattjus
 */
public class GenresHelper {

    private static final int INDEX_ID = 0;
    private static final int INDEX_MOVIE_ID = 1;
    private static final int INDEX_GENRE_ID = 2;
    private static final int INDEX_NAME = 3;

    private static final String TAG = GenresHelper.class.getSimpleName();

    /**
     * Method for writing a a list of genres to the database
     *
     * @param context
     * @param movieId
     * @param genres
     */
    public static void write(Context context, Integer movieId, List<Genre> genres) {

        Log.d(TAG, "write - movieId \"" + movieId + "\" genres \"" + JsonUtility.toJson(genres) + "\"");

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        for(com.example.android.popularmovies.data.Genre genre : genres) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(GenresEntry.MOVIE_ID, movieId);
            contentValues.put(GenresEntry.GENRE_ID, genre.getId());
            contentValues.put(GenresEntry.NAME, genre.getName());
        }

        context.getContentResolver().bulkInsert(GenresEntry.CONTENT_URI, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
    }

    /**
     * Remove genres for a specific movie
     *
     * @param context
     * @param movieId
     */
    public static void remove(Context context, Integer movieId) {

        Log.d(TAG, "remove - movieId: " + movieId);

        if(movieId == null) {
            return;
        }

        context.getContentResolver().delete(
                GenresEntry.CONTENT_URI,
                GenresEntry.MOVIE_ID + " = ?",
                new String[] { movieId.toString() });
    }

    /**
     * Get all genres for a movie from it's id
     *
     * @param context
     * @param movieId
     * @return
     */
    public static List<com.example.android.popularmovies.data.Genre> getGenresFromMovieId(Context context, Integer movieId) {

        Log.d(TAG, "getGenresFromMovieId - movieId: " + movieId);
        List<com.example.android.popularmovies.data.Genre> genres = new ArrayList<>();

        if (movieId == null) {
            return genres;
        }

        Cursor cursor = context.getContentResolver().query(
                GenresEntry.CONTENT_URI,
                null,
                GenresEntry.MOVIE_ID + " = ?",
                new String[]{movieId.toString()},
                null);

        try {
            if (cursor == null || !cursor.moveToFirst()) {
                return genres;
            }

            genres.add(getGenreFromCursor(cursor));
            while (cursor.moveToNext()) {
                genres.add(getGenreFromCursor(cursor));
            }

            return genres;
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Get a Genre object from a cursor
     *
     * @param cursor
     * @return
     */
    private static com.example.android.popularmovies.data.Genre getGenreFromCursor(Cursor cursor) {
        Integer id = cursor.getInt(INDEX_GENRE_ID);
        String name = cursor.getString(INDEX_NAME);

        return new com.example.android.popularmovies.data.Genre(id, name);
    }
}
