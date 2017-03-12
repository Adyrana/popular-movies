package com.example.android.popularmovies.data.db.videos;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * @author Julia Mattjus
 */
public interface VideosColumns {
    @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";

    @DataType(INTEGER) @NotNull String MOVIE_ID = "movie_id";

    @DataType(TEXT) @NotNull String VIDEO_ID = "video_id";

    @DataType(TEXT) String ISO_639_1 = "iso_639_1";

    @DataType(TEXT) String ISO_3166_1 = "iso_3166_1";

    @DataType(TEXT) String KEY = "key";

    @DataType(TEXT) @NotNull String NAME = "name";

    @DataType(TEXT) @NotNull String SITE = "site";

    @DataType(INTEGER) String SIZE = "size";

    @DataType(TEXT) @NotNull String TYPE = "type";
}
