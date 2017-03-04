package com.example.android.popularmovies.data.db.productioncompanies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.data.ProductionCompanies;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julia Mattjus
 */
@ContentProvider(authority = ProductionCompaniesProvider.AUTHORITY, database = ProductionCompaniesDatabase.class)
public final class ProductionCompaniesProvider {

    @TableEndpoint(table = ProductionCompaniesDatabase.PRODUCTION_COMPANIES) public static class ProductionCompanies {

        @ContentUri(
                path = "production_companies",
                type = "vnd.android.cursor.dir/production_companies",
                defaultSort = ProductionCompaniesColumns._ID
        )
        public static final Uri PRODUCTION_COMPANIES = Uri.parse("content://" + AUTHORITY + "/production_companies");
    }

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.productioncompanies.ProductionCompaniesProvider";

    public static final int INDEX_ID = 0;
    public static final int INDEX_MOVIE_ID = 1;
    public static final int INDEX_PRODUCTION_COMPANIES_ID = 2;
    public static final int INDEX_NAME = 3;

    private static final String TAG = ProductionCompaniesProvider.class.getSimpleName();

    public static void write(Context context, Integer movieId, List<com.example.android.popularmovies.data.ProductionCompanies> productionCompanies) {

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        for(com.example.android.popularmovies.data.ProductionCompanies productionCompanie : productionCompanies) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ProductionCompaniesColumns.MOVIE_ID, movieId);
            contentValues.put(ProductionCompaniesColumns.PRODUCTION_COMPANIES_ID, productionCompanie.getId());
            contentValues.put(ProductionCompaniesColumns.NAME, productionCompanie.getName());
        }

        context.getContentResolver().bulkInsert(ProductionCompanies.PRODUCTION_COMPANIES, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
    }

    public static void remove(Context context, Integer movieId) {

        Log.d(TAG, "remove - movieId: " + movieId);

        if(movieId == null) {
            return;
        }

        context.getContentResolver().delete(
                ProductionCompanies.PRODUCTION_COMPANIES,
                ProductionCompaniesColumns.MOVIE_ID + " = ?",
                new String[] { movieId.toString() });
    }

    public static List<com.example.android.popularmovies.data.ProductionCompanies> getProductionCompaniesFromMovieId(Context context, Integer movieId) {

        Log.d(TAG, "getProductionCompaniesFromMovieId - movieId: " + movieId);
        List<com.example.android.popularmovies.data.ProductionCompanies> productionCompanies = new ArrayList<>();

        if(movieId == null) {
            return productionCompanies;
        }

        Cursor cursor = context.getContentResolver().query(
                ProductionCompanies.PRODUCTION_COMPANIES,
                null,
                ProductionCompaniesColumns.MOVIE_ID + " = ?",
                new String[] { movieId.toString() },
                null);

        if(cursor == null || !cursor.moveToFirst()) {
            return productionCompanies;
        }

        productionCompanies.add(getProductionCompaniesFromCursor(cursor));
        while(cursor.moveToNext()) {
            productionCompanies.add(getProductionCompaniesFromCursor(cursor));
        }

        return productionCompanies;
    }

    private static com.example.android.popularmovies.data.ProductionCompanies getProductionCompaniesFromCursor(Cursor cursor) {
        Integer id = cursor.getInt(INDEX_PRODUCTION_COMPANIES_ID);
        String name = cursor.getString(INDEX_NAME);

        return new com.example.android.popularmovies.data.ProductionCompanies(id, name);
    }
}