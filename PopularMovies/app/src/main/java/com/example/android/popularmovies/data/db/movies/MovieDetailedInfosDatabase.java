package com.example.android.popularmovies.data.db.movies;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * @author Julia Mattjus
 */
@Database(version = MovieDetailedInfosDatabase.VERSION)
public final class MovieDetailedInfosDatabase {
    public static final int VERSION = 1;

    @Table(MovieDetailedInfosColumns.class) public static final String MOVIE_DETAILED_INFOS = "movie_detailed_infos";
}
