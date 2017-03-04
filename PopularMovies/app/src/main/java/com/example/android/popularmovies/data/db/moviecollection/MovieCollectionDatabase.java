package com.example.android.popularmovies.data.db.moviecollection;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * @author Julia Mattjus
 */
@Database(version = MovieCollectionDatabase.VERSION)
public final class MovieCollectionDatabase {
    public static final int VERSION = 1;

    @Table(MovieCollectionColumns.class) public static final String MOVIE_COLLECTION = "movie_collection";
}
