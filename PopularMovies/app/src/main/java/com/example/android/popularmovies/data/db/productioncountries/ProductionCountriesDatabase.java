package com.example.android.popularmovies.data.db.productioncountries;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * @author Julia Mattjus
 */
@Database(version = ProductionCountriesDatabase.VERSION)
public final class ProductionCountriesDatabase {
    public static final int VERSION = 1;

    @Table(ProductionCountriesColumns.class) public static final String PRODUCTION_COUNTRIES = "production_countries";
}
