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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.popularmovies.data.ProductionCountries;
import com.example.android.popularmovies.data.db.productioncountries.ProductionCountriesContract.ProductionCountriesEntry;
import com.example.android.popularmovies.utilities.JsonUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julia Mattjus
 */
public class ProductionCountriesHelper {

    private static final int INDEX_ID = 0;
    private static final int INDEX_MOVIE_ID = 1;
    private static final int INDEX_ISO_3166_1 = 2;
    private static final int INDEX_NAME = 3;

    private static final String TAG = ProductionCountriesHelper.class.getSimpleName();

    /**
     * Method for writing a list of production countries to the database
     *
     * @param context
     * @param movieId
     * @param productionCountries
     */
    public static void write(Context context, Integer movieId, List<ProductionCountries> productionCountries) {

        Log.d(TAG, "write - movieId \"" + movieId + "\" genres \"" + JsonUtility.toJson(productionCountries) + "\"");

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        for(com.example.android.popularmovies.data.ProductionCountries productionCountry : productionCountries) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ProductionCountriesEntry.MOVIE_ID, movieId);
            contentValues.put(ProductionCountriesEntry.ISO_3166_1, productionCountry.getIso31661());
            contentValues.put(ProductionCountriesEntry.NAME, productionCountry.getName());
            contentValuesList.add(contentValues);
        }

        context.getContentResolver().bulkInsert(ProductionCountriesEntry.CONTENT_URI, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
    }

    /**
     * Method for removing production countries for a movie by the movie id
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
                ProductionCountriesEntry.CONTENT_URI,
                ProductionCountriesEntry.MOVIE_ID + " = ?",
                new String[] { movieId.toString() });
    }

    /**
     * Method for getting production countries for a movie by the movie id
     *
     * @param context
     * @param movieId
     * @return
     */
    public static List<com.example.android.popularmovies.data.ProductionCountries> getProductionCountriesFromMovieId(Context context, Integer movieId) {

        Log.d(TAG, "getProductionCountriesFromMovieId - movieId: " + movieId);
        List<com.example.android.popularmovies.data.ProductionCountries> productionCountries = new ArrayList<>();

        if(movieId == null) {
            return productionCountries;
        }

        Cursor cursor = context.getContentResolver().query(
                ProductionCountriesEntry.CONTENT_URI,
                null,
                ProductionCountriesEntry.MOVIE_ID + " = ?",
                new String[] { movieId.toString() },
                null);

        try {
            if (cursor == null || !cursor.moveToFirst()) {
                return productionCountries;
            }

            productionCountries.add(getProductionCountriesFromCursor(cursor));
            while (cursor.moveToNext()) {
                productionCountries.add(getProductionCountriesFromCursor(cursor));
            }

            return productionCountries;
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Method for getting a production country from a cursor
     *
     * @param cursor
     * @return
     */
    private static com.example.android.popularmovies.data.ProductionCountries getProductionCountriesFromCursor(Cursor cursor) {
        String iso31661 = cursor.getString(INDEX_ISO_3166_1);
        String name = cursor.getString(INDEX_NAME);

        return new com.example.android.popularmovies.data.ProductionCountries(iso31661, name);
    }
}
