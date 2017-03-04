package com.example.android.popularmovies.data.db.spokenlanguages;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * @author Julia Mattjus
 */
@Database(version = SpokenLanguagesDatabase.VERSION)
public final class SpokenLanguagesDatabase {
    public static final int VERSION = 1;

    @Table(SpokenLanguagesColumns.class) public static final String SPOKEN_LANGUAGES = "spoken_languages";
}
