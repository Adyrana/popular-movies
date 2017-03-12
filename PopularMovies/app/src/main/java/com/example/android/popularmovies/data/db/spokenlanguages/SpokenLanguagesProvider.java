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

package com.example.android.popularmovies.data.db.spokenlanguages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.utilities.JsonUtility;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julia Mattjus
 */
@ContentProvider(authority = SpokenLanguagesProvider.AUTHORITY, database = SpokenLanguagesDatabase.class)
public final class SpokenLanguagesProvider {
    @TableEndpoint(table = SpokenLanguagesDatabase.SPOKEN_LANGUAGES) public static class SpokenLanguages {

        @ContentUri(
                path = "spoken_languages",
                type = "vnd.android.cursor.dir/spoken_languages",
                defaultSort = SpokenLanguagesColumns._ID
        )
        public static final Uri MOVIE_DETAILED_INFOS = Uri.parse("content://" + AUTHORITY + "/spoken_languages");
    }

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.spokenlanguages.SpokenLanguagesProvider";

    private static final int INDEX_ID = 0;
    private static final int INDEX_MOVIE_ID = 1;
    private static final int INDEX_ISO_639_1 = 2;
    private static final int INDEX_NAME = 3;

    private static final String TAG = SpokenLanguagesProvider.class.getSimpleName();

    /**
     * Method for writing a list of spoken languages for a movie to the database
     *
     * @param context
     * @param movieId
     * @param spokenLanguages
     */
    public static void write(Context context, Integer movieId, List<com.example.android.popularmovies.data.SpokenLanguages> spokenLanguages) {

        Log.d(TAG, "write - movieId \"" + movieId + "\" genres \"" + JsonUtility.toJson(spokenLanguages) + "\"");

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        for(com.example.android.popularmovies.data.SpokenLanguages spokenLanguage : spokenLanguages) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SpokenLanguagesColumns.MOVIE_ID, movieId);
            contentValues.put(SpokenLanguagesColumns.ISO_639_1, spokenLanguage.getIso6391());
            contentValues.put(SpokenLanguagesColumns.NAME, spokenLanguage.getName());
            contentValuesList.add(contentValues);
        }

        context.getContentResolver().bulkInsert(SpokenLanguages.MOVIE_DETAILED_INFOS, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
    }

    /**
     * Method for removing spoken languages for a movie by the movie id
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
                SpokenLanguages.MOVIE_DETAILED_INFOS,
                SpokenLanguagesColumns.MOVIE_ID + " = ?",
                new String[] { movieId.toString() });
    }

    /**
     * Method for getting spoken languages for a movie by the movie id
     *
     * @param context
     * @param movieId
     * @return
     */
    public static List<com.example.android.popularmovies.data.SpokenLanguages> getSpokenLanguagesFromMovieId(Context context, Integer movieId) {

        Log.d(TAG, "getSpokenLanguagesFromMovieId - movieId: " + movieId);
        List<com.example.android.popularmovies.data.SpokenLanguages> spokenLanguages = new ArrayList<>();

        if(movieId == null) {
            return spokenLanguages;
        }

        Cursor cursor = context.getContentResolver().query(
                SpokenLanguages.MOVIE_DETAILED_INFOS,
                null,
                SpokenLanguagesColumns.MOVIE_ID + " = ?",
                new String[] { movieId.toString() },
                null);

        try {
            if (cursor == null || !cursor.moveToFirst()) {
                return spokenLanguages;
            }

            spokenLanguages.add(getSpokenLanguagesFromCursor(cursor));
            while (cursor.moveToNext()) {
                spokenLanguages.add(getSpokenLanguagesFromCursor(cursor));
            }

            return spokenLanguages;
        } finally {
            cursor.close();
        }
    }

    /**
     * Method for getting spoken languages from a cursor
     *
     * @param cursor
     * @return
     */
    private static com.example.android.popularmovies.data.SpokenLanguages getSpokenLanguagesFromCursor(Cursor cursor) {
        String iso6391 = cursor.getString(INDEX_ISO_639_1);
        String name = cursor.getString(INDEX_NAME);

        return new com.example.android.popularmovies.data.SpokenLanguages(iso6391, name);
    }
}
