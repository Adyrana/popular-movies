package com.example.android.popularmovies.data.db.reviews;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * @author Julia Mattjus
 */
public interface ReviewsColumns {
    @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";

    @DataType(INTEGER) @NotNull String MOVIE_ID = "movie_id";

    @DataType(TEXT) @NotNull String REVIEW_ID = "review_id";

    @DataType(TEXT) @NotNull String AUTHOR = "author";

    @DataType(TEXT) @NotNull String CONTENT = "content";

    @DataType(TEXT) String ISO_639_1 = "iso_639_1";

    @DataType(TEXT) String MEDIA_ID = "media_id";

    @DataType(TEXT) String MEDIA_TITLE = "media_title";

    @DataType(TEXT) String MEDIA_TYPE = "media_type";

    @DataType(TEXT) String URL = "url";
}
