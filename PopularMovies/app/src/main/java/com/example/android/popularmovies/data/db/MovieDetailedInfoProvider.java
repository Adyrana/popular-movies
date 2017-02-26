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

import static net.simonvt.schematic.annotation.DataType.Type.BLOB;
import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * @author Julia Mattjus
 */
public class MovieDetailedInfoProvider {
    public interface MovieDetailedInfoColumns {
        @DataType(INTEGER) @PrimaryKey String _ID = "_id";

        @DataType(INTEGER) @NotNull String ADULT = "adult";

        @DataType(TEXT) @NotNull String BACKDROP_PATH = "backdrop_path";

        @DataType(BLOB) @NotNull String BELONGS_TO_COLLECTION = "belongs_to_collection"; // TODO: Reference here

        @DataType(INTEGER) @NotNull String BUDGET = "budget";

        @DataType(TEXT) @NotNull String GENRES = "genres"; // TODO: What to do here?

        @DataType(TEXT) @NotNull String HOMEPAGE = "homepage";

        @DataType(TEXT) @NotNull String IMDB_ID = "imdb_id";

        @DataType(TEXT) @NotNull String ORIGINAL_LANGUAGE = "original_language";

        @DataType(TEXT) @NotNull String ORIGINAL_TITLE = "original_title";

        @DataType(TEXT) @NotNull String OVERVIEW = "overview";

        @DataType(REAL) @NotNull String POPULARITY = "popularity";

        @DataType(TEXT) @NotNull String POSTER_PATH = "poster_path";

        @DataType(TEXT) @NotNull String PRODUCTION_COMPANIES = "production_companies"; // TODO: What to do here?

        @DataType(TEXT) @NotNull String PRODUCTION_COUNTRIES = "production_countries"; // TODO: What to do here?

        @DataType(TEXT) @NotNull String RELEASE_DATE = "release_date";

        @DataType(INTEGER) @NotNull String REVENUE = "revenue";

        @DataType(INTEGER) @NotNull String RUNTIME = "runtime";

        @DataType(TEXT) @NotNull String SPOKEN_LANGUAGE = "spoken_languages"; // TODO: What to do here?

        @DataType(TEXT) @NotNull String STATUS = "status";

        @DataType(TEXT) @NotNull String TAGLINE = "tagline";

        @DataType(TEXT) @NotNull String TITLE = "title";

        @DataType(INTEGER) @NotNull String VIDEO = "video";

        @DataType(REAL) @NotNull String VOTE_AVERAGE = "vote_average";

        @DataType(INTEGER) @NotNull String VOTE_COUNT = "vote_count";

        @DataType(TEXT) @NotNull String VIDEOS = "videos"; // TODO: What to do here?

        @DataType(TEXT) @NotNull String REVIEWS = "reviews"; // TODO: What to do here?
    }

    @Database(version = MovieDetailedInfoDatabase.VERSION)
    public final class MovieDetailedInfoDatabase {
        public static final int VERSION = 1;

        @Table(MovieDetailedInfoColumns.class) public static final String MOVIE_DETAILED_INFO = "movie_detailed_info";
    }

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.MovieDetailedInfoProvider";

    @TableEndpoint(table = MovieDetailedInfoDatabase.MOVIE_DETAILED_INFO) public static class MovieDetailedInfo {

        @ContentUri(
                path = "movie_detailed_info",
                type = "vnd.android.cursor.dir/movie_detailed_info",
                defaultSort = MovieDetailedInfoColumns._ID
        )
        public static final Uri MOVIE_DETAILED_INFO = Uri.parse("content://" + AUTHORITY + "/movie_detailed_info");
    }
}
