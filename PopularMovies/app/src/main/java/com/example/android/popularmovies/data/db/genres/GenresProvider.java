package com.example.android.popularmovies.data.db.genres;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.example.android.popularmovies.data.Genre;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julia Mattjus
 */
@ContentProvider(authority = GenresProvider.AUTHORITY, database = GenresDatabase.class)
public final class GenresProvider {

    @TableEndpoint(table = GenresDatabase.GENRES) public static class Genres {

        @ContentUri(
                path = "genres",
                type = "vnd.android.cursor.dir/genres",
                defaultSort = GenresColumns._ID
        )
        public static final Uri GENRES = Uri.parse("content://" + AUTHORITY + "/genres");
    }

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.genres.GenresProvider";

    public static final int INDEX_ID = 0;
    public static final int INDEX_MOVIE_ID = 1;
    public static final int INDEX_GENRE_ID = 2;
    public static final int INDEX_NAME = 3;

    private static final String TAG = GenresProvider.class.getSimpleName();

    public static void write(Context context, Integer movieId, List<Genre> genres) {

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        for(com.example.android.popularmovies.data.Genre genre : genres) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(GenresColumns.MOVIE_ID, movieId);
            contentValues.put(GenresColumns.GENRE_ID, genre.getId());
            contentValues.put(GenresColumns.NAME, genre.getName());
        }

        context.getContentResolver().bulkInsert(Genres.GENRES, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
    }

    public static void remove(Context context, Integer movieId) {

        Log.d(TAG, "remove - movieId: " + movieId);

        if(movieId == null) {
            return;
        }

        context.getContentResolver().delete(
                Genres.GENRES,
                GenresColumns.MOVIE_ID + " = ?",
                new String[] { movieId.toString() });
    }

    public static List<com.example.android.popularmovies.data.Genre> getGenresFromMovieId(Context context, Integer movieId) {

        Log.d(TAG, "getGenresFromMovieId - movieId: " + movieId);
        List<com.example.android.popularmovies.data.Genre> genres = new ArrayList<>();

        if(movieId == null) {
            return genres;
        }

        Cursor cursor = context.getContentResolver().query(
                Genres.GENRES,
                null,
                GenresColumns.MOVIE_ID + " = ?",
                new String[] { movieId.toString() },
                null);

        if(cursor == null || !cursor.moveToFirst()) {
            return genres;
        }

        genres.add(getGenreFromCursor(cursor));
        while(cursor.moveToNext()) {
            genres.add(getGenreFromCursor(cursor));
        }

        return genres;
    }

    private static com.example.android.popularmovies.data.Genre getGenreFromCursor(Cursor cursor) {
        Integer id = cursor.getInt(INDEX_GENRE_ID);
        String name = cursor.getString(INDEX_NAME);

        return new com.example.android.popularmovies.data.Genre(id, name);
    }
}
