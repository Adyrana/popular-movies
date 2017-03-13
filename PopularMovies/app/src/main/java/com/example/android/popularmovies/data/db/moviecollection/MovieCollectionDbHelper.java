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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.db.moviecollection.MovieCollectionContract.MovieCollectionEntry;

/**
 * @author Julia Mattjus
 */
public class MovieCollectionDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movie_collection.db";

    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor
     *
     * @param context
     */
    public MovieCollectionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_TABLE =
                "create table " + MovieCollectionEntry.TABLE_NAME + " (" +
                        MovieCollectionEntry._ID + " integer primary key autoincrement, " +
                        MovieCollectionEntry.MOVIE_ID + " integer not null, " +
                        MovieCollectionEntry.MOVIE_COLLECTION_ID + " integer not null, " +
                        MovieCollectionEntry.NAME + " text not null, " +
                        MovieCollectionEntry.POSTER_PATH + " text, " +
                        MovieCollectionEntry.BACKDROP_PATH + " text);";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists " + MovieCollectionEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
