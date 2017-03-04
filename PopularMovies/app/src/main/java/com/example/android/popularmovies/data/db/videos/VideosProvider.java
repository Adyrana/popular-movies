package com.example.android.popularmovies.data.db.videos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.android.popularmovies.data.Video;
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

    public static final int INDEX_VIDEO_ID = 0;
    public static final int INDEX_VIDEO_ISO_639_1 = 1;
    public static final int INDEX_VIDEO_ISO_3166_1 = 2;
    public static final int INDEX_VIDEO_KEY = 3;
    public static final int INDEX_VIDEO_NAME = 4;
    public static final int INDEX_VIDEO_SITE = 5;
    public static final int INDEX_VIDEO_SIZE = 6;
    public static final int INDEX_VIDEO_TYPE = 7;

    private static final String TAG = VideosProvider.class.getSimpleName();

    public static void write(Context context, com.example.android.popularmovies.data.Videos videos) {

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        for(Video video : videos.getResults()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(VideosColumns._ID, video.getId());
            contentValues.put(VideosColumns.KEY, video.getKey());
            contentValues.put(VideosColumns.ISO_639_1, video.getIso6391());
            contentValues.put(VideosColumns.ISO_3166_1, video.getIso31661());
            contentValues.put(VideosColumns.NAME, video.getName());
            contentValues.put(VideosColumns.SITE, video.getSite());
            contentValues.put(VideosColumns.SIZE, video.getSize());
            contentValues.put(VideosColumns.TYPE, video.getType());
        }

        context.getContentResolver().bulkInsert(Videos.VIDEOS, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
    }

    public static void remove(Context context, List<String> videosIds) {

        Log.d(TAG, "remove - videosIds: " + JsonUtility.toJson(videosIds.toArray(new String[videosIds.size()])));

        if(videosIds == null || videosIds.size() == 0) {
            return;
        }

        context.getContentResolver().delete(
                Videos.VIDEOS,
                buildSelection(videosIds),
                videosIds.toArray(new String[videosIds.size()]));
    }

    public static com.example.android.popularmovies.data.Videos getVideosFromFromIds(Context context, List<String> videosIds) {

        Log.d(TAG, "getVideosFromFromIds - videosIds: " + JsonUtility.toJson(videosIds));
        List<Video> videosList = new ArrayList<>();

        if(videosIds == null || videosIds.size() == 0) {
            return new com.example.android.popularmovies.data.Videos(videosList);
        }

        Cursor cursor = context.getContentResolver().query(
                Videos.VIDEOS,
                null,
                buildSelection(videosIds),
                videosIds.toArray(new String[videosIds.size()]),
                null);

        if(cursor == null || !cursor.moveToFirst()) {
            return new com.example.android.popularmovies.data.Videos(videosList);
        }


        videosList.add(getVideoFromCursor(cursor));
        while(cursor.moveToNext()) {
            videosList.add(getVideoFromCursor(cursor));
        }

        return new com.example.android.popularmovies.data.Videos(videosList);
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

    private static String buildSelection(List<String> ids) {
        String selection = VideosColumns._ID + " in (";
        for(int i = 0; i < ids.size(); i++) {
            selection += "?, ";
        }
        return selection.substring(0, selection.length() - 2) + ")";
    }
}