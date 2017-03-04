package com.example.android.popularmovies.data.db.videos;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * @author Julia Mattjus
 */
@Database(version = VideosDatabase.VERSION)
public final class VideosDatabase {
    public static final int VERSION = 1;

    @Table(VideosColumns.class) public static final String VIDEOS = "videos";
}