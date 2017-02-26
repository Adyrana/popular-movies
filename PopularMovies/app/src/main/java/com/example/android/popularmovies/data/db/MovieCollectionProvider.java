/*
 * Copyright 2014 Simon Vig Therkildsen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.popularmovies.data.db;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Table;
import net.simonvt.schematic.annotation.TableEndpoint;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * @author Julia Mattjus
 */
public class MovieCollectionProvider {
    public interface MovieCollectionColumns {
        @DataType(INTEGER) @PrimaryKey String _ID = "_id";

        @DataType(TEXT) @NotNull String NAME = "name";

        @DataType(TEXT) @NotNull String POSTER_PATH = "poster_path";

        @DataType(TEXT) @NotNull String BACKDROP_PATH = "backdrop_path";
    }

    @Database(version = MovieCollectionDatabase.VERSION)
    public final class MovieCollectionDatabase {
        public static final int VERSION = 1;

        @Table(MovieCollectionColumns.class) public static final String MOVIE_COLLECTION = "movie_collection";
    }

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.MovieCollectionProvider";

    @TableEndpoint(table = MovieCollectionDatabase.MOVIE_COLLECTION) public static class MovieCollection {

        @ContentUri(
                path = "movie_collection",
                type = "vnd.android.cursor.dir/movie_collection",
                defaultSort = MovieCollectionColumns._ID
        )
        public static final Uri MOVIE_COLLECTION = Uri.parse("content://" + AUTHORITY + "/movie_collection");
    }
}
