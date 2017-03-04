package com.example.android.popularmovies.data.db.videos;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * @author Julia Mattjus
 */
public interface VideosColumns {
    @DataType(TEXT) @PrimaryKey String _ID = "_id";

    @DataType(TEXT) @NotNull String ISO_639_1 = "iso_639_1";

    @DataType(TEXT) @NotNull String ISO_3166_1 = "iso_3166_1";

    @DataType(TEXT) @NotNull String KEY = "key";

    @DataType(TEXT) @NotNull String NAME = "name";

    @DataType(TEXT) @NotNull String SITE = "site";

    @DataType(INTEGER) @NotNull String SIZE = "size";

    @DataType(TEXT) @NotNull String TYPE = "type";
}
