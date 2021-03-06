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


package com.example.android.popularmovies.data.db.videos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.popularmovies.data.Video;
import com.example.android.popularmovies.data.db.videos.VideosContract.VideosEntry;
import com.example.android.popularmovies.utilities.JsonUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julia Mattjus
 */
public class VideosHelper {

    private static final int INDEX_ID = 0;
    private static final int INDEX_MOVIE_ID = 1;
    private static final int INDEX_VIDEO_ID = 2;
    private static final int INDEX_VIDEO_ISO_639_1 = 3;
    private static final int INDEX_VIDEO_ISO_3166_1 = 4;
    private static final int INDEX_VIDEO_KEY = 5;
    private static final int INDEX_VIDEO_NAME = 6;
    private static final int INDEX_VIDEO_SITE = 7;
    private static final int INDEX_VIDEO_SIZE = 8;
    private static final int INDEX_VIDEO_TYPE = 9;

    private static final String TAG = VideosHelper.class.getSimpleName();

    /**
     * Method for writing a set of videos to the database
     *
     * @param context
     * @param movieId
     * @param videos
     */
    public static void write(Context context, Integer movieId, com.example.android.popularmovies.data.Videos videos) {

        Log.d(TAG, "write - movieId \"" + movieId + "\" genres \"" + JsonUtility.toJson(videos) + "\"");

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        for(Video video : videos.getResults()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(VideosEntry.MOVIE_ID, movieId);
            contentValues.put(VideosEntry.VIDEO_ID, video.getId());
            contentValues.put(VideosEntry.KEY, video.getKey());
            contentValues.put(VideosEntry.ISO_639_1, video.getIso6391());
            contentValues.put(VideosEntry.ISO_3166_1, video.getIso31661());
            contentValues.put(VideosEntry.NAME, video.getName());
            contentValues.put(VideosEntry.SITE, video.getSite());
            contentValues.put(VideosEntry.SIZE, video.getSize());
            contentValues.put(VideosEntry.TYPE, video.getType());
            contentValuesList.add(contentValues);
        }

        context.getContentResolver().bulkInsert(VideosEntry.CONTENT_URI, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
    }

    /**
     * Method for removing videos from the database for a movie by the movie id
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
                VideosEntry.CONTENT_URI,
                VideosEntry.MOVIE_ID + " = ?",
                new String[] { movieId.toString() });
    }

    /**
     * Mmethod for getting a set of videos for a movie by the movie id
     *
     * @param context
     * @param movieId
     * @return
     */
    public static com.example.android.popularmovies.data.Videos getVideosFromFromMovieId(Context context, Integer movieId) {

        Log.d(TAG, "getReviewsFromFromMovieId - movieId: " + movieId);
        List<Video> videosList = new ArrayList<>();

        if(movieId == null) {
            return new com.example.android.popularmovies.data.Videos(videosList);
        }

        Cursor cursor = context.getContentResolver().query(
                VideosEntry.CONTENT_URI,
                null,
                VideosEntry.MOVIE_ID + " = ?",
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
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Method for getting a video from a cursor
     *
     * @param cursor
     * @return
     */
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
