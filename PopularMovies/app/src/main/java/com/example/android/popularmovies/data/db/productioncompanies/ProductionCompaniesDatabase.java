package com.example.android.popularmovies.data.db.productioncompanies;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * @author Julia Mattjus
 */
@Database(version = ProductionCompaniesDatabase.VERSION)
public final class ProductionCompaniesDatabase {
    public static final int VERSION = 1;

    @Table(ProductionCompaniesColumns.class) public static final String PRODUCTION_COMPANIES = "production_companies";
}
