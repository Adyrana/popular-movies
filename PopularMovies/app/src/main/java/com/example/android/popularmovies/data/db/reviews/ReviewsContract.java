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

package com.example.android.popularmovies.data.db.reviews;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Julia Mattjus
 */
public class ReviewsContract {

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.reviews.ReviewsProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String REVIEWS = "reviews";

    public static final class ReviewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(REVIEWS)
                .build();

        public static final String TABLE_NAME = "reviews";

        public static final String _ID = "_id";

        public static final String MOVIE_ID = "movie_id";

        public static final String REVIEW_ID = "review_id";

        public static final String AUTHOR = "author";

        public static final String CONTENT = "content";

        public static final String ISO_639_1 = "iso_639_1";

        public static final String MEDIA_ID = "media_id";

        public static final String MEDIA_TITLE = "media_title";

        public static final String MEDIA_TYPE = "media_type";

        public static final String URL = "url";
    }
}
