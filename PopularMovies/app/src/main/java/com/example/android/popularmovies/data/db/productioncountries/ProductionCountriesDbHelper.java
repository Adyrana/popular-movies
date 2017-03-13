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

package com.example.android.popularmovies.data.db.productioncountries;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.db.productioncountries.ProductionCountriesContract.ProductionCountriesEntry;

/**
 * @author Julia Mattjus
 */
public class ProductionCountriesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "production_countries.db";

    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor
     *
     * @param context
     */
    public ProductionCountriesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_TABLE =
                "create table " + ProductionCountriesEntry.TABLE_NAME + " (" +
                        ProductionCountriesEntry._ID + " integer primary key autoincrement, " +
                        ProductionCountriesEntry.MOVIE_ID + " integer not null, " +
                        ProductionCountriesEntry.ISO_3166_1 + " text not null, " +
                        ProductionCountriesEntry.NAME + " text not null);";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists " + ProductionCountriesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
