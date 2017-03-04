package com.example.android.popularmovies.data.db.reviews;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * @author Julia Mattjus
 */
@Database(version = ReviewsDatabase.VERSION)
public final class ReviewsDatabase {
    public static final int VERSION = 1;

    @Table(ReviewsColumns.class) public static final String REVIEWS = "reviews";
}
