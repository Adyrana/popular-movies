package com.example.android.popularmovies.data.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.android.popularmovies.data.Video;
import com.example.android.popularmovies.data.Videos;
import com.example.android.popularmovies.data.db.VideosProvider;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

/**
 * @author Julia Mattjus
 */
@AllArgsConstructor
public class VideosLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context mContext;
    private MovieLoader movieLoader;
    private List<Integer> videoIds;

    private static final String TAG = VideosLoader.class.getSimpleName();

    private static final int ID_VIDEOS_LOADER = 355;

    public static final int INDEX_VIDEO_ID = 0;
    public static final int INDEX_VIDEO_ISO_639_1 = 1;
    public static final int INDEX_VIDEO_ISO_3166_1 = 2;
    public static final int INDEX_VIDEO_KEY = 3;
    public static final int INDEX_VIDEO_NAME = 4;
    public static final int INDEX_VIDEO_SITE = 5;
    public static final int INDEX_VIDEO_SIZE = 6;
    public static final int INDEX_VIDEO_TYPE = 7;

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        List<String> whereArgs = new ArrayList<String>();
        for(Integer videoId : videoIds) {
            whereArgs.add(videoId.toString());
        }

        return new CursorLoader(mContext,
                VideosProvider.Videos.VIDEOS,
                null,
                "_id = ?",
                whereArgs.toArray(new String[videoIds.size()]),
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }

        List<Video> videoList = new ArrayList<Video>();
        while(data.moveToNext()) {
            int id = data.getInt(INDEX_VIDEO_ID);
            String iso6391 = data.getString(INDEX_VIDEO_ISO_639_1);
            String iso31661 = data.getString(INDEX_VIDEO_ISO_3166_1);
            String key = data.getString(INDEX_VIDEO_KEY);
            String name = data.getString(INDEX_VIDEO_NAME);
            String site = data.getString(INDEX_VIDEO_SITE);
            int size = data.getInt(INDEX_VIDEO_SIZE);
            String type = data.getString(INDEX_VIDEO_TYPE);

            videoList.add(new Video(id, iso6391, iso31661, key, name, site, size, type));
        }

        Videos videos = new Videos(videoList);

        movieLoader.setVideos(videos);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
