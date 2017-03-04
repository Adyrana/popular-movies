package com.example.android.popularmovies.data.db.genres;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * @author Julia Mattjus
 */
@Database(version = GenresDatabase.VERSION)
public final class GenresDatabase {
    public static final int VERSION = 1;

    @Table(GenresColumns.class) public static final String GENRES = "genres";
}
