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
public class ReviewProvider {
    public interface ReviewColumns {
        @DataType(INTEGER) @PrimaryKey String _ID = "_id";

        @DataType(TEXT) @NotNull String AUTHOR = "author";

        @DataType(TEXT) @NotNull String CONTENT = "content";

        @DataType(TEXT) @NotNull String ISO_639_1 = "iso_639_1";

        @DataType(TEXT) @NotNull String MEDIA_ID = "media_id";

        @DataType(TEXT) @NotNull String MEDIA_TITLE = "media_title";

        @DataType(TEXT) @NotNull String MEDIA_TYPE = "media_type";

        @DataType(TEXT) @NotNull String URL = "url";
    }

    @Database(version = ReviewDatabase.VERSION)
    public final class ReviewDatabase {
        public static final int VERSION = 1;

        @Table(ReviewColumns.class) public static final String REVIEW = "review";
    }

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.ReviewProvider";

    @TableEndpoint(table = ReviewDatabase.REVIEW) public static class Review {

        @ContentUri(
                path = "review",
                type = "vnd.android.cursor.dir/review",
                defaultSort = ReviewColumns._ID
        )
        public static final Uri REVIEW = Uri.parse("content://" + AUTHORITY + "/review");
    }
}
