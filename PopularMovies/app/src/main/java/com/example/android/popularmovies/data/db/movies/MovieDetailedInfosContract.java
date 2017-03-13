/*
 * Copyright (C) 2017 Julia Mattjus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.popularmovies.data.db.movies;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Julia Mattjus
 */
public class MovieDetailedInfosContract {

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.movies.MovieDetailedInfosProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String MOVIE_DETAILED_INFOS = "movie_detailed_infos";

    public static final class MovieDetailedInfosEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(MOVIE_DETAILED_INFOS)
                .build();

        public static final String TABLE_NAME = "movie_detailed_infos";

        public static final String _ID = "_id";

        public static final String ADULT = "adult";

        public static final String BACKDROP_PATH = "backdrop_path";

        public static final String BELONGS_TO_COLLECTION = "belongs_to_collection";

        public static final String BUDGET = "budget";

        public static final String GENRES = "genres";

        public static final String HOMEPAGE = "homepage";

        public static final String IMDB_ID = "imdb_id";

        public static final String ORIGINAL_LANGUAGE = "original_language";

        public static final String ORIGINAL_TITLE = "original_title";

        public static final String OVERVIEW = "overview";

        public static final String POPULARITY = "popularity";

        public static final String POSTER_PATH = "poster_path";

        public static final String PRODUCTION_COMPANIES = "production_companies";

        public static final String PRODUCTION_COUNTRIES = "production_countries";

        public static final String RELEASE_DATE = "release_date";

        public static final String REVENUE = "revenue";

        public static final String RUNTIME = "runtime";

        public static final String SPOKEN_LANGUAGES = "spoken_languages";

        public static final String STATUS = "status";

        public static final String TAGLINE = "tagline";

        public static final String TITLE = "title";

        public static final String VIDEO = "video";

        public static final String VOTE_AVERAGE = "vote_average";

        public static final String VOTE_COUNT = "vote_count";

        public static final String VIDEOS = "videos";

        public static final String REVIEWS = "reviews";
    }
}
