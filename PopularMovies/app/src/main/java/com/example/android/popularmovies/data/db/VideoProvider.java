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
 * Created by Julia on 2017-02-26.
 */

public class VideoProvider {
    public interface VideoColumns {
        @DataType(INTEGER) @PrimaryKey String _ID = "_id";

        @DataType(TEXT) @NotNull String ISO_639_1 = "iso_639_1";

        @DataType(TEXT) @NotNull String ISO_3166_1 = "iso_3166_1";

        @DataType(TEXT) @NotNull String KEY = "key";

        @DataType(TEXT) @NotNull String NAME = "name";

        @DataType(TEXT) @NotNull String SITE = "site";

        @DataType(INTEGER) @NotNull String SIZE = "size";

        @DataType(TEXT) @NotNull String TYPE = "type";
    }

    @Database(version = VideoDatabase.VERSION)
    public final class VideoDatabase {
        public static final int VERSION = 1;

        @Table(VideoColumns.class) public static final String VIDEO = "video";
    }

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.VideoProvider";

    @TableEndpoint(table = VideoDatabase.VIDEO) public static class Video {

        @ContentUri(
                path = "video",
                type = "vnd.android.cursor.dir/video",
                defaultSort = VideoColumns._ID
        )
        public static final Uri VIDEO = Uri.parse("content://" + AUTHORITY + "/video");
    }
}
