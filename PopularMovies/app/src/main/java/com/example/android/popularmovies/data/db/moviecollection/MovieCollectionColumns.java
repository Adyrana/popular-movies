package com.example.android.popularmovies.data.db.moviecollection;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * @author Julia Mattjus
 */
public interface MovieCollectionColumns {
    @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";

    @DataType(INTEGER) @NotNull String MOVIE_ID = "movie_id";

    @DataType(INTEGER) @NotNull String MOVIE_COLLECTION_ID = "movie_collection_id";

    @DataType(TEXT) @NotNull String NAME = "name";

    @DataType(TEXT) String POSTER_PATH = "posterPath";

    @DataType(TEXT) String BACKDROP_PATH = "backdrop_path";
}
