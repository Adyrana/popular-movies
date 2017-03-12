package com.example.android.popularmovies.data.db.moviecollection;

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
@ContentProvider(authority = MovieCollectionProvider.AUTHORITY, database = MovieCollectionDatabase.class)
public final class MovieCollectionProvider {

    @TableEndpoint(table = MovieCollectionDatabase.MOVIE_COLLECTION) public static class MovieCollection {

        @ContentUri(
                path = "movie_collection",
                type = "vnd.android.cursor.dir/movie_collection",
                defaultSort = MovieCollectionColumns._ID
        )
        public static final Uri MOVIE_COLLECTION = Uri.parse("content://" + AUTHORITY + "/movie_collection");
    }

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.moviecollection.MovieCollectionProvider";

    public static final int INDEX_ID = 0;
    public static final int INDEX_MOVIE_ID = 1;
    public static final int INDEX_MOVIE_COLLECTION_ID = 2;
    public static final int INDEX_NAME = 3;
    public static final int INDEX_POSTER_PATH = 4;
    public static final int INDEX_BACKDROP_PATH = 5;

    private static final String TAG = MovieCollectionProvider.class.getSimpleName();

    /**
     * Method for writing a movie collection to the database
     *
     * @param context
     * @param movieId
     * @param movieCollection
     */
    public static void write(Context context, Integer movieId, com.example.android.popularmovies.data.MovieCollection movieCollection) {

        Log.d(TAG, "write - movieCollection: " + JsonUtility.toJson(movieCollection));

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieCollectionColumns.MOVIE_ID, movieId);
        contentValues.put(MovieCollectionColumns.MOVIE_COLLECTION_ID, movieCollection.getId());
        contentValues.put(MovieCollectionColumns.NAME, getStringOrEmpty(movieCollection.getName()));
        contentValues.put(MovieCollectionColumns.POSTER_PATH, getStringOrEmpty(movieCollection.getPosterPath()));
        contentValues.put(MovieCollectionColumns.BACKDROP_PATH,getStringOrEmpty( movieCollection.getBackdropPath()));

        context.getContentResolver().insert(MovieCollection.MOVIE_COLLECTION, contentValues);
    }

    /**
     * Helper method to make sure we don't have any null values but rather empty strings in case of null
     *
     * @param in
     * @return
     */
    private static String getStringOrEmpty(String in) {
        return in != null ? in : "";
    }

    public static void remove(Context context, Integer movieId) {

        Log.d(TAG, "remove - movieId: " + movieId);

        if(movieId == null) {
            return;
        }

        context.getContentResolver().delete(
                MovieCollection.MOVIE_COLLECTION,
                MovieCollectionColumns.MOVIE_ID + " = ?",
                new String[] { movieId.toString() });
    }

    /**
     * Get a movie collection for a movie by the movie id
     *
     * @param context
     * @param movieId
     * @return
     */
    public static com.example.android.popularmovies.data.MovieCollection getMovieCollectionFromMovieId(Context context, Integer movieId) {

        Log.d(TAG, "getMovieCollectionFromMovieId - movieId: " + movieId);

        if(movieId == null) {
            return null;
        }

        Cursor cursor = context.getContentResolver().query(
                MovieCollection.MOVIE_COLLECTION,
                null,
                MovieCollectionColumns.MOVIE_ID + " = ?",
                new String[] { movieId.toString() },
                null);

        try {
            if(cursor == null || !cursor.moveToFirst()) {
                return null;
            }

            return getMovieCollectionFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }

    /**
     * Method for getting a movie collection from a cursor
     *
     * @param cursor
     * @return
     */
    private static com.example.android.popularmovies.data.MovieCollection getMovieCollectionFromCursor(Cursor cursor) {
        Integer id = cursor.getInt(INDEX_MOVIE_COLLECTION_ID);
        String name = cursor.getString(INDEX_NAME);
        String posterPath = cursor.getString(INDEX_POSTER_PATH);
        String backdropPath = cursor.getString(INDEX_BACKDROP_PATH);

        return new com.example.android.popularmovies.data.MovieCollection(id, name, posterPath, backdropPath);
    }
}
