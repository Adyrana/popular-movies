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

package com.example.android.popularmovies.data.db.movies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.db.movies.MovieDetailedInfosContract.MovieDetailedInfosEntry;

/**
 * @author Julia Mattjus
 */
public class MovieDetailedInfosDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movie_detailed_infos.db";

    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor
     *
     * @param context
     */
    public MovieDetailedInfosDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_TABLE =
                "create table " + MovieDetailedInfosEntry.TABLE_NAME + " (" +
                        MovieDetailedInfosEntry._ID + " integer primary key, " +
                        MovieDetailedInfosEntry.ADULT + " integer not null, " +
                        MovieDetailedInfosEntry.BACKDROP_PATH + " text not null, " +
                        MovieDetailedInfosEntry.BELONGS_TO_COLLECTION + " integer not null, " +
                        MovieDetailedInfosEntry.BUDGET + " integer not null, " +
                        MovieDetailedInfosEntry.GENRES + " integer not null, " +
                        MovieDetailedInfosEntry.HOMEPAGE + " text not null, " +
                        MovieDetailedInfosEntry.IMDB_ID + " text not null, " +
                        MovieDetailedInfosEntry.ORIGINAL_LANGUAGE + " text not null, " +
                        MovieDetailedInfosEntry.ORIGINAL_TITLE + " text not null, " +
                        MovieDetailedInfosEntry.OVERVIEW + " text not null, " +
                        MovieDetailedInfosEntry.POPULARITY + " real not null, " +
                        MovieDetailedInfosEntry.POSTER_PATH + " text not null, " +
                        MovieDetailedInfosEntry.PRODUCTION_COMPANIES + " integer not null, " +
                        MovieDetailedInfosEntry.PRODUCTION_COUNTRIES + " integer not null, " +
                        MovieDetailedInfosEntry.RELEASE_DATE + " text not null, " +
                        MovieDetailedInfosEntry.REVENUE + " integer not null, " +
                        MovieDetailedInfosEntry.RUNTIME + " integer not null, " +
                        MovieDetailedInfosEntry.SPOKEN_LANGUAGES + " integer not null, " +
                        MovieDetailedInfosEntry.STATUS + " text not null, " +
                        MovieDetailedInfosEntry.TAGLINE + " text not null, " +
                        MovieDetailedInfosEntry.TITLE + " text not null, " +
                        MovieDetailedInfosEntry.VIDEO + " integer not null, " +
                        MovieDetailedInfosEntry.VOTE_AVERAGE + " real not null, " +
                        MovieDetailedInfosEntry.VOTE_COUNT + " integer not null, " +
                        MovieDetailedInfosEntry.VIDEOS + " integer not null, " +
                        MovieDetailedInfosEntry.REVIEWS + " integer not null);";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists " + MovieDetailedInfosEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
