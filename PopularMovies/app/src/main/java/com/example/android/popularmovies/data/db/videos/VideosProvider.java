package com.example.android.popularmovies.data.db.videos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.android.popularmovies.data.Video;
import com.example.android.popularmovies.data.db.reviews.ReviewsColumns;
import com.example.android.popularmovies.utilities.JsonUtility;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julia Mattjus
 */
@ContentProvider(authority = VideosProvider.AUTHORITY, database = VideosDatabase.class)
public final class VideosProvider {

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.videos.VideosProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    @TableEndpoint(table = VideosDatabase.VIDEOS) public static class Videos {

        @ContentUri(
                path = "videos",
                type = "vnd.android.cursor.dir/videos",
                defaultSort = VideosColumns._ID
        )
        public static final Uri VIDEOS = Uri.parse("content://" + AUTHORITY + "/videos");
    }

    public static final int INDEX_ID = 0;
    public static final int INDEX_MOVIE_ID = 1;
    public static final int INDEX_VIDEO_ID = 2;
    public static final int INDEX_VIDEO_ISO_639_1 = 3;
    public static final int INDEX_VIDEO_ISO_3166_1 = 4;
    public static final int INDEX_VIDEO_KEY = 5;
    public static final int INDEX_VIDEO_NAME = 6;
    public static final int INDEX_VIDEO_SITE = 7;
    public static final int INDEX_VIDEO_SIZE = 8;
    public static final int INDEX_VIDEO_TYPE = 9;

    private static final String TAG = VideosProvider.class.getSimpleName();

    public static void write(Context context, Integer movieId, com.example.android.popularmovies.data.Videos videos) {

        Log.d(TAG, "write - movieId \"" + movieId + "\" genres \"" + JsonUtility.toJson(videos) + "\"");

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        for(Video video : videos.getResults()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(VideosColumns.MOVIE_ID, movieId);
            contentValues.put(VideosColumns.VIDEO_ID, video.getId());
            contentValues.put(VideosColumns.KEY, video.getKey());
            contentValues.put(VideosColumns.ISO_639_1, video.getIso6391());
            contentValues.put(VideosColumns.ISO_3166_1, video.getIso31661());
            contentValues.put(VideosColumns.NAME, video.getName());
            contentValues.put(VideosColumns.SITE, video.getSite());
            contentValues.put(VideosColumns.SIZE, video.getSize());
            contentValues.put(VideosColumns.TYPE, video.getType());
            contentValuesList.add(contentValues);
        }

        context.getContentResolver().bulkInsert(Videos.VIDEOS, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
    }

    public static void remove(Context context, Integer movieId) {

        Log.d(TAG, "remove - movieId: " + movieId);

        if(movieId == null) {
            return;
        }

        context.getContentResolver().delete(
                Videos.VIDEOS,
                ReviewsColumns.MOVIE_ID + " = ?",
                new String[] { movieId.toString() });
    }

    public static com.example.android.popularmovies.data.Videos getVideosFromFromMovieId(Context context, Integer movieId) {

        Log.d(TAG, "getReviewsFromFromMovieId - movieId: " + movieId);
        List<Video> videosList = new ArrayList<>();

        if(movieId == null) {
            return new com.example.android.popularmovies.data.Videos(videosList);
        }

        Cursor cursor = context.getContentResolver().query(
                Videos.VIDEOS,
                null,
                ReviewsColumns.MOVIE_ID + " = ?",
                new String[] { movieId.toString() },
                null);

        try {
            if (cursor == null || !cursor.moveToFirst()) {
                return new com.example.android.popularmovies.data.Videos(videosList);
            }

            videosList.add(getVideoFromCursor(cursor));
            while (cursor.moveToNext()) {
                videosList.add(getVideoFromCursor(cursor));
            }

            return new com.example.android.popularmovies.data.Videos(videosList);
        } finally {
            cursor.close();
        }
    }

    private static Video getVideoFromCursor(Cursor cursor) {
        String id = cursor.getString(INDEX_VIDEO_ID);
        String iso6391 = cursor.getString(INDEX_VIDEO_ISO_639_1);
        String iso31661 = cursor.getString(INDEX_VIDEO_ISO_3166_1);
        String key = cursor.getString(INDEX_VIDEO_KEY);
        String name = cursor.getString(INDEX_VIDEO_NAME);
        String site = cursor.getString(INDEX_VIDEO_SITE);
        int size = cursor.getInt(INDEX_VIDEO_SIZE);
        String type = cursor.getString(INDEX_VIDEO_TYPE);

        return new Video(id, iso6391, iso31661, key, name, site, size, type);
    }
}