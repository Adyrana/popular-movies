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
public class ProductionCompaniesProvider {
    public interface ProductionCompaniesColumns {
        @DataType(INTEGER) @PrimaryKey String _ID = "_id";

        @DataType(TEXT) @NotNull String NAME = "name";
    }

    @Database(version = ProductionCompaniesDatabase.VERSION)
    public final class ProductionCompaniesDatabase {
        public static final int VERSION = 1;

        @Table(ProductionCompaniesColumns.class) public static final String PRODUCTION_COMPANIES = "production_companies";
    }

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.ProductionCompaniesProvider";

    @TableEndpoint(table = ProductionCompaniesDatabase.PRODUCTION_COMPANIES) public static class ProductionCompanies {

        @ContentUri(
                path = "production_companies",
                type = "vnd.android.cursor.dir/production_companies",
                defaultSort = ProductionCompaniesColumns._ID
        )
        public static final Uri PRODUCTION_COMPANIES = Uri.parse("content://" + AUTHORITY + "/production_companies");
    }
}
