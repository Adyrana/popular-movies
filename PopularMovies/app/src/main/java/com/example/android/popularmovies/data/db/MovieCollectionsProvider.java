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
public class MovieCollectionsProvider {
    public interface MovieCollectionsColumns {
        @DataType(INTEGER) @PrimaryKey String _ID = "_id";

        @DataType(TEXT) @NotNull String NAME = "name";

        @DataType(TEXT) @NotNull String POSTER_PATH = "poster_path";

        @DataType(TEXT) @NotNull String BACKDROP_PATH = "backdrop_path";
    }

    @Database(version = MovieCollectionsDatabase.VERSION)
    public final class MovieCollectionsDatabase {
        public static final int VERSION = 1;

        @Table(MovieCollectionsColumns.class) public static final String MOVIE_COLLECTIONS = "movie_collections";
    }

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.MovieCollectionsProvider";

    @TableEndpoint(table = MovieCollectionsDatabase.MOVIE_COLLECTIONS) public static class MovieCollections {

        @ContentUri(
                path = "movie_collections",
                type = "vnd.android.cursor.dir/movie_collections",
                defaultSort = MovieCollectionsColumns._ID
        )
        public static final Uri MOVIE_COLLECTIONS = Uri.parse("content://" + AUTHORITY + "/movie_collections");
    }
}
