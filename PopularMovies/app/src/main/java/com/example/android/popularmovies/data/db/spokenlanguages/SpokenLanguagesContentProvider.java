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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.popularmovies.data.db.spokenlanguages.SpokenLanguagesContract.SpokenLanguagesEntry;

/**
 * @author Julia Mattjus
 */
public class SpokenLanguagesContentProvider extends ContentProvider {

    public static final int SPOKEN_LANGUAGES = 700;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SpokenLanguagesDbHelper mOpenHelper;

    /**
     * Build the uri matcher
     *
     * @return
     */
    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SpokenLanguagesContract.AUTHORITY;

        matcher.addURI(authority, SpokenLanguagesContract.SPOKEN_LANGUAGES, SPOKEN_LANGUAGES);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new SpokenLanguagesDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case SPOKEN_LANGUAGES:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(SpokenLanguagesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case SPOKEN_LANGUAGES:
                cursor = mOpenHelper.getReadableDatabase().query(
                        SpokenLanguagesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("GetType isn't implemented for Popular Movies.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new RuntimeException("Insert isn't implemented for Popular Movies, use bulkInsert instead.");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        switch (sUriMatcher.match(uri)) {
            case SPOKEN_LANGUAGES:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        SpokenLanguagesEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("Update isn't implemented for Popular Movies.");
    }
}
