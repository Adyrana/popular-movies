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

package com.example.android.popularmovies.data.db.productioncompanies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.popularmovies.data.ProductionCompanies;
import com.example.android.popularmovies.data.db.productioncompanies.ProductionCompaniesContract.ProductionCompaniesEntry;
import com.example.android.popularmovies.utilities.JsonUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julia Mattjus
 */
public class ProductionCompaniesHelper {

    private static final int INDEX_ID = 0;
    private static final int INDEX_MOVIE_ID = 1;
    private static final int INDEX_PRODUCTION_COMPANIES_ID = 2;
    private static final int INDEX_NAME = 3;

    private static final String TAG = ProductionCompaniesHelper.class.getSimpleName();

    /**
     * Method for writing a list of production companies to the database
     *
     * @param context
     * @param movieId
     * @param productionCompanies
     */
    public static void write(Context context, Integer movieId, List<ProductionCompanies> productionCompanies) {

        Log.d(TAG, "write - movieId \"" + movieId + "\" genres \"" + JsonUtility.toJson(productionCompanies) + "\"");

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        for(com.example.android.popularmovies.data.ProductionCompanies productionCompany : productionCompanies) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ProductionCompaniesEntry.MOVIE_ID, movieId);
            contentValues.put(ProductionCompaniesEntry.PRODUCTION_COMPANIES_ID, productionCompany.getId());
            contentValues.put(ProductionCompaniesEntry.NAME, productionCompany.getName());
            contentValuesList.add(contentValues);
        }

        context.getContentResolver().bulkInsert(ProductionCompaniesEntry.CONTENT_URI, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
    }

    /**
     * Rmove production company database entries by a movie id
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
                ProductionCompaniesEntry.CONTENT_URI,
                ProductionCompaniesEntry.MOVIE_ID + " = ?",
                new String[] { movieId.toString() });
    }

    /**
     * Mthod for getting production companies for a movie by the movie id
     *
     * @param context
     * @param movieId
     * @return
     */
    public static List<com.example.android.popularmovies.data.ProductionCompanies> getProductionCompaniesFromMovieId(Context context, Integer movieId) {

        Log.d(TAG, "getProductionCompaniesFromMovieId - movieId: " + movieId);
        List<com.example.android.popularmovies.data.ProductionCompanies> productionCompanies = new ArrayList<>();

        if(movieId == null) {
            return productionCompanies;
        }

        Cursor cursor = context.getContentResolver().query(
                ProductionCompaniesEntry.CONTENT_URI,
                null,
                ProductionCompaniesEntry.MOVIE_ID + " = ?",
                new String[] { movieId.toString() },
                null);

        try {
            if (cursor == null || !cursor.moveToFirst()) {
                return productionCompanies;
            }

            productionCompanies.add(getProductionCompaniesFromCursor(cursor));
            while (cursor.moveToNext()) {
                productionCompanies.add(getProductionCompaniesFromCursor(cursor));
            }

            return productionCompanies;
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Method for getting a production company from a cursor
     *
     * @param cursor
     * @return
     */
    private static com.example.android.popularmovies.data.ProductionCompanies getProductionCompaniesFromCursor(Cursor cursor) {
        Integer id = cursor.getInt(INDEX_PRODUCTION_COMPANIES_ID);
        String name = cursor.getString(INDEX_NAME);

        return new com.example.android.popularmovies.data.ProductionCompanies(id, name);
    }
}
