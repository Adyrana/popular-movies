package com.example.android.popularmovies.data.db.productioncountries;

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
@ContentProvider(authority = ProductionCountriesProvider.AUTHORITY, database = ProductionCountriesDatabase.class)
public final class ProductionCountriesProvider {

    @TableEndpoint(table = ProductionCountriesDatabase.PRODUCTION_COUNTRIES) public static class ProductionCountries {

        @ContentUri(
                path = "production_countries",
                type = "vnd.android.cursor.dir/production_countries",
                defaultSort = ProductionCountriesColumns._ID
        )
        public static final Uri PRODUCTION_COUNTRIES = Uri.parse("content://" + AUTHORITY + "/production_countries");
    }

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.productioncountries.ProductionCountriesProvider";

    public static final int INDEX_ID = 0;
    public static final int INDEX_MOVIE_ID = 1;
    public static final int INDEX_ISO_3166_1 = 2;
    public static final int INDEX_NAME = 3;

    private static final String TAG = ProductionCountriesProvider.class.getSimpleName();

    /**
     * Method for writing a list of production countries to the database
     *
     * @param context
     * @param movieId
     * @param productionCountries
     */
    public static void write(Context context, Integer movieId, List<com.example.android.popularmovies.data.ProductionCountries> productionCountries) {

        Log.d(TAG, "write - movieId \"" + movieId + "\" genres \"" + JsonUtility.toJson(productionCountries) + "\"");

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        for(com.example.android.popularmovies.data.ProductionCountries productionCountry : productionCountries) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ProductionCountriesColumns.MOVIE_ID, movieId);
            contentValues.put(ProductionCountriesColumns.ISO_3166_1, productionCountry.getIso31661());
            contentValues.put(ProductionCountriesColumns.NAME, productionCountry.getName());
            contentValuesList.add(contentValues);
        }

        context.getContentResolver().bulkInsert(ProductionCountries.PRODUCTION_COUNTRIES, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
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
                ProductionCountries.PRODUCTION_COUNTRIES,
                ProductionCountriesColumns.MOVIE_ID + " = ?",
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
                ProductionCountries.PRODUCTION_COUNTRIES,
                null,
                ProductionCountriesColumns.MOVIE_ID + " = ?",
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
            cursor.close();
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
