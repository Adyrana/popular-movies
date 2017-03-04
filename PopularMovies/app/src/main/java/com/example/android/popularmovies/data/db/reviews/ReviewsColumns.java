package com.example.android.popularmovies.data.db.reviews;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * @author Julia Mattjus
 */
public interface ReviewsColumns {
    @DataType(TEXT) @PrimaryKey String _ID = "_id";

    @DataType(TEXT) @NotNull String AUTHOR = "author";

    @DataType(TEXT) @NotNull String CONTENT = "content";

    @DataType(TEXT) @NotNull String ISO_639_1 = "iso_639_1";

    @DataType(TEXT) @NotNull String MEDIA_ID = "media_id";

    @DataType(TEXT) @NotNull String MEDIA_TITLE = "media_title";

    @DataType(TEXT) @NotNull String MEDIA_TYPE = "media_type";

    @DataType(TEXT) @NotNull String URL = "url";
}
