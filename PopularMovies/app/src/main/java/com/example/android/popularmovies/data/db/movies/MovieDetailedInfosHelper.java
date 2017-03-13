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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.popularmovies.data.Genre;
import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.MovieCollection;
import com.example.android.popularmovies.data.MovieDetailedInfo;
import com.example.android.popularmovies.data.ProductionCompanies;
import com.example.android.popularmovies.data.ProductionCountries;
import com.example.android.popularmovies.data.Reviews;
import com.example.android.popularmovies.data.SpokenLanguages;
import com.example.android.popularmovies.data.Videos;
import com.example.android.popularmovies.data.db.genres.GenresHelper;
import com.example.android.popularmovies.data.db.moviecollection.MovieCollectionHelper;
import com.example.android.popularmovies.data.db.productioncompanies.ProductionCompaniesHelper;
import com.example.android.popularmovies.data.db.productioncountries.ProductionCountriesHelper;
import com.example.android.popularmovies.data.db.reviews.ReviewsHelper;
import com.example.android.popularmovies.data.db.spokenlanguages.SpokenLanguagesHelper;
import com.example.android.popularmovies.data.db.videos.VideosHelper;
import com.example.android.popularmovies.utilities.JsonUtility;

import java.util.ArrayList;
import java.util.List;

import com.example.android.popularmovies.data.db.movies.MovieDetailedInfosContract.MovieDetailedInfosEntry;

/**
 * @author Julia Mattjus
 */
public class MovieDetailedInfosHelper {

    private static final int INDEX_MOVIE_INFO_ID = 0;
    private static final int INDEX_MOVIE_INFO_ADULT = 1;
    private static final int INDEX_MOVIE_INFO_BACKDROP_PATH = 2;
    private static final int INDEX_MOVIE_INFO_BELONGS_TO_COLLECTION = 3;
    private static final int INDEX_MOVIE_INFO_BUDGET = 4;
    private static final int INDEX_MOVIE_INFO_GENRES = 5;
    private static final int INDEX_MOVIE_INFO_HOMEPAGE = 6;
    private static final int INDEX_MOVIE_INFO_IMDB_ID = 7;
    private static final int INDEX_MOVIE_INFO_ORIGINAL_LANGUAGE = 8;
    private static final int INDEX_MOVIE_INFO_ORIGINAL_TITLE = 9;
    private static final int INDEX_MOVIE_INFO_OVERVIEW = 10;
    private static final int INDEX_MOVIE_INFO_POPULARITY = 11;
    private static final int INDEX_MOVIE_INFO_POSTER_PATH = 12;
    private static final int INDEX_MOVIE_INFO_PRODUCTION_COMPANIES = 13;
    private static final int INDEX_MOVIE_INFO_PRODUCTION_COUNTRIES = 14;
    private static final int INDEX_MOVIE_INFO_RELEASE_DATE = 15;
    private static final int INDEX_MOVIE_INFO_REVENUE = 16;
    private static final int INDEX_MOVIE_INFO_RUNTIME = 17;
    private static final int INDEX_MOVIE_INFO_SPOKEN_LANGUAGES = 18;
    private static final int INDEX_MOVIE_INFO_STATUS = 19;
    private static final int INDEX_MOVIE_INFO_TAGLINE = 20;
    private static final int INDEX_MOVIE_INFO_TITLE = 21;
    private static final int INDEX_MOVIE_INFO_VIDEO = 22;
    private static final int INDEX_MOVIE_INFO_VOTE_AVERAGE = 23;
    private static final int INDEX_MOVIE_INFO_VOTE_COUNT = 24;
    private static final int INDEX_MOVIE_INFO_VIDEOS = 25;
    private static final int INDEX_MOVIE_INFO_REVIEWS = 26;

    private static final int INDEX_MAIN_MOVIE_PROJECTION_ID = 0;
    private static final int INDEX_MAIN_MOVIE_PROJECTION_POSTER_PATH = 1;

    private static final String[] MAIN_MOVIES_PROJECTION = {
            MovieDetailedInfosEntry._ID,
            MovieDetailedInfosEntry.POSTER_PATH
    };

    private static final String TAG = MovieDetailedInfosHelper.class.getSimpleName();

    /**
     * Method for writing a movie to the database with all of it's child objects into separate tables
     *
     * @param context
     * @param movieDetailedInfo
     */
    public static void write(Context context, MovieDetailedInfo movieDetailedInfo) {

        Log.d(TAG, "write - movieDetailedInfo: " + JsonUtility.toJson(movieDetailedInfo));

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieDetailedInfosEntry._ID, movieDetailedInfo.getId());
        contentValues.put(MovieDetailedInfosEntry.ADULT, movieDetailedInfo.getAdult() ? 1 : 0);
        contentValues.put(MovieDetailedInfosEntry.BACKDROP_PATH, movieDetailedInfo.getBackdropPath());
        contentValues.put(MovieDetailedInfosEntry.BUDGET, movieDetailedInfo.getBudget());
        contentValues.put(MovieDetailedInfosEntry.HOMEPAGE, movieDetailedInfo.getHomepage());
        contentValues.put(MovieDetailedInfosEntry.IMDB_ID, movieDetailedInfo.getImdbId());
        contentValues.put(MovieDetailedInfosEntry.ORIGINAL_LANGUAGE, movieDetailedInfo.getOriginalLanguage());
        contentValues.put(MovieDetailedInfosEntry.ORIGINAL_TITLE, movieDetailedInfo.getOriginalTitle());
        contentValues.put(MovieDetailedInfosEntry.OVERVIEW, movieDetailedInfo.getOverview());
        contentValues.put(MovieDetailedInfosEntry.POPULARITY, movieDetailedInfo.getPopularity());
        contentValues.put(MovieDetailedInfosEntry.POSTER_PATH, movieDetailedInfo.getPosterPath());
        contentValues.put(MovieDetailedInfosEntry.HOMEPAGE, movieDetailedInfo.getHomepage());
        contentValues.put(MovieDetailedInfosEntry.IMDB_ID, movieDetailedInfo.getImdbId());
        contentValues.put(MovieDetailedInfosEntry.RELEASE_DATE, movieDetailedInfo.getReleaseDate());
        contentValues.put(MovieDetailedInfosEntry.REVENUE, movieDetailedInfo.getRevenue());
        contentValues.put(MovieDetailedInfosEntry.RUNTIME, movieDetailedInfo.getRuntime());
        contentValues.put(MovieDetailedInfosEntry.STATUS, movieDetailedInfo.getStatus());
        contentValues.put(MovieDetailedInfosEntry.TAGLINE, movieDetailedInfo.getTagline());
        contentValues.put(MovieDetailedInfosEntry.TITLE, movieDetailedInfo.getTitle());
        contentValues.put(MovieDetailedInfosEntry.VIDEO, movieDetailedInfo.getVideo() ? 1 : 0);
        contentValues.put(MovieDetailedInfosEntry.VOTE_AVERAGE, movieDetailedInfo.getVoteAverage());
        contentValues.put(MovieDetailedInfosEntry.VOTE_COUNT, movieDetailedInfo.getVoteCount());

        if(movieDetailedInfo.getGenres() != null) {
            GenresHelper.write(context, movieDetailedInfo.getId(), movieDetailedInfo.getGenres());
            contentValues.put(MovieDetailedInfosEntry.GENRES, movieDetailedInfo.getId());
        } else {
            contentValues.put(MovieDetailedInfosEntry.GENRES, -1);
        }

        if(movieDetailedInfo.getBelongsToCollection() != null) {
            MovieCollectionHelper.write(context, movieDetailedInfo.getId(), movieDetailedInfo.getBelongsToCollection());
            contentValues.put(MovieDetailedInfosEntry.BELONGS_TO_COLLECTION, movieDetailedInfo.getId());
        } else {
            contentValues.put(MovieDetailedInfosEntry.BELONGS_TO_COLLECTION, -1);
        }

        if(movieDetailedInfo.getProductionCompanies() != null) {
            ProductionCompaniesHelper.write(context, movieDetailedInfo.getId(), movieDetailedInfo.getProductionCompanies());
            contentValues.put(MovieDetailedInfosEntry.PRODUCTION_COMPANIES, movieDetailedInfo.getId());
        } else {
            contentValues.put(MovieDetailedInfosEntry.PRODUCTION_COMPANIES, -1);
        }

        if(movieDetailedInfo.getProductionCountries() != null) {
            ProductionCountriesHelper.write(context, movieDetailedInfo.getId(), movieDetailedInfo.getProductionCountries());
            contentValues.put(MovieDetailedInfosEntry.PRODUCTION_COUNTRIES, movieDetailedInfo.getId());
        } else {
            contentValues.put(MovieDetailedInfosEntry.PRODUCTION_COUNTRIES, -1);
        }

        if(movieDetailedInfo.getSpokenLanguages() != null) {
            SpokenLanguagesHelper.write(context, movieDetailedInfo.getId(), movieDetailedInfo.getSpokenLanguages());
            contentValues.put(MovieDetailedInfosEntry.SPOKEN_LANGUAGES, movieDetailedInfo.getId());
        } else {
            contentValues.put(MovieDetailedInfosEntry.SPOKEN_LANGUAGES, -1);
        }

        if(movieDetailedInfo.getReviews().getResults() != null && movieDetailedInfo.getReviews().getResults().size() > 0) {
            ReviewsHelper.write(context, movieDetailedInfo.getId(), movieDetailedInfo.getReviews());
            contentValues.put(MovieDetailedInfosEntry.REVIEWS, movieDetailedInfo.getId());
        } else {
            contentValues.put(MovieDetailedInfosEntry.REVIEWS, -1);
        }

        if(movieDetailedInfo.getVideos().getResults() != null && movieDetailedInfo.getVideos().getResults().size() > 0) {
            VideosHelper.write(context, movieDetailedInfo.getId(), movieDetailedInfo.getVideos());
            contentValues.put(MovieDetailedInfosEntry.VIDEOS, movieDetailedInfo.getId());
        } else {
            contentValues.put(MovieDetailedInfosEntry.VIDEOS, -1);
        }

        context.getContentResolver().insert(MovieDetailedInfosEntry.CONTENT_URI, contentValues);
    }

    /**
     * Get a cursor with all favourite movies, that is, all of the movies in the database
     *
     * @param context
     * @return
     */
    public static Cursor getFavouritesCursor(Context context) {
        String sortOrder = MovieDetailedInfosEntry.TITLE + " ASC";

        Cursor cursor = context.getContentResolver().query(
                MovieDetailedInfosEntry.CONTENT_URI,
                MAIN_MOVIES_PROJECTION,
                null,
                null,
                sortOrder);

        if(cursor == null) {
            return null;
        }

        return cursor;
    }

    /**
     * Helper method for getting the movie id from a main projection cursor
     *
     * @param cursor
     * @return
     */
    public static Integer getMovieIdFromMainProjectionCursor(Cursor cursor) {
        return cursor.getInt(INDEX_MAIN_MOVIE_PROJECTION_ID);
    }

    /**
     * Helper method for getting the poster path from a main projection cursor
     *
     * @param cursor
     * @return
     */
    public static String getPosterPathFromMainProjectionCursor(Cursor cursor) {
        return cursor.getString(INDEX_MAIN_MOVIE_PROJECTION_POSTER_PATH);
    }

    /**
     * Method that fetches all favourite movies from the database (that is, all of the movies)
     *
     * @param context
     * @return
     */
    public static List<Movie> getFavourites(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MovieDetailedInfosEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        try {
            if (cursor == null || !cursor.moveToFirst()) {
                return null;
            }

            List<Movie> favouriteMovies = new ArrayList<>();

            favouriteMovies.add(getMovieFromCursor(context, cursor));

            while (cursor.moveToNext()) {
                favouriteMovies.add(getMovieFromCursor(context, cursor));
            }

            return favouriteMovies;
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Get a movie by it's id
     *
     * @param context
     * @param movieId
     * @return
     */
    public static MovieDetailedInfo getFavourite(Context context, Integer movieId) {

        Log.d(TAG, "getFavourite - movieId: " + movieId);

        Cursor cursor = context.getContentResolver().query(
                MovieDetailedInfosEntry.CONTENT_URI,
                null,
                MovieDetailedInfosEntry._ID + " = ?",
                new String[] { movieId.toString() },
                null);

        try {
            if (cursor == null || !cursor.moveToFirst()) {
                return null;
            }

            return getDetailedMovieFromCursor(context, cursor);
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Remove a movie from the database and all of the child objects corresponding database entries
     *
     * @param context
     * @param movieDetailedInfo
     */
    public static void removeFavourite(Context context, MovieDetailedInfo movieDetailedInfo) {
        remove(context, movieDetailedInfo.getId());

        GenresHelper.remove(context, movieDetailedInfo.getId());
        MovieCollectionHelper.remove(context, movieDetailedInfo.getId());
        ProductionCompaniesHelper.remove(context, movieDetailedInfo.getId());
        ProductionCountriesHelper.remove(context, movieDetailedInfo.getId());
        SpokenLanguagesHelper.remove(context, movieDetailedInfo.getId());
        VideosHelper.remove(context, movieDetailedInfo.getId());
        ReviewsHelper.remove(context, movieDetailedInfo.getId());
    }

    /**
     * Method that removes a movie from the detailed infos table
     *
     * @param context
     * @param movieId
     * @return
     */
    private static int remove(Context context, Integer movieId) {
        return context.getContentResolver().delete(
                MovieDetailedInfosEntry.CONTENT_URI,
                MovieDetailedInfosEntry._ID + " = ?",
                new String[] { movieId.toString() });
    }

    /**
     * Method that gets a movie from a cursor, the smaller type of object representing a movie
     *
     * @param context
     * @param cursor
     * @return
     */
    public static Movie getMovieFromCursor(Context context, Cursor cursor) {

        Integer id = cursor.getInt(INDEX_MOVIE_INFO_ID);
        Boolean adult = cursor.getInt(INDEX_MOVIE_INFO_ADULT) == 1;
        String backdropPath = cursor.getString(INDEX_MOVIE_INFO_BACKDROP_PATH);
        String originalLanguage = cursor.getString(INDEX_MOVIE_INFO_ORIGINAL_LANGUAGE);
        String originalTitle = cursor.getString(INDEX_MOVIE_INFO_ORIGINAL_TITLE);
        String overview = cursor.getString(INDEX_MOVIE_INFO_OVERVIEW);
        Double popularity = cursor.getDouble(INDEX_MOVIE_INFO_POPULARITY);
        String posterPath = cursor.getString(INDEX_MOVIE_INFO_POSTER_PATH);
        String releaseDate = cursor.getString(INDEX_MOVIE_INFO_RELEASE_DATE);
        String title = cursor.getString(INDEX_MOVIE_INFO_TITLE);
        Boolean video = cursor.getInt(INDEX_MOVIE_INFO_VIDEO) == 1;
        Double voteAverage = cursor.getDouble(INDEX_MOVIE_INFO_VOTE_AVERAGE);
        Integer voteCount = cursor.getInt(INDEX_MOVIE_INFO_VOTE_COUNT);

        List<Genre> genres = GenresHelper.getGenresFromMovieId(context, id);
        Integer[] genreIds = new Integer[genres.size()];
        for(int i = 0; i < genres.size(); i++) {
            genreIds[i] = genres.get(i).getId();
        }

        Movie movie = new Movie(posterPath, adult, overview, releaseDate, genreIds, id, originalTitle,
                originalLanguage, title, backdropPath, popularity, voteCount, video, voteAverage);

        return movie;
    }

    /**
     * Get a movie object from the database, in this case, the complete version of a object representing an movie
     *
     * @param context
     * @param cursor
     * @return
     */
    private static MovieDetailedInfo getDetailedMovieFromCursor(Context context, Cursor cursor) {
        Integer id = cursor.getInt(INDEX_MOVIE_INFO_ID);
        Boolean adult = cursor.getInt(INDEX_MOVIE_INFO_ADULT) == 1;
        String backdropPath = cursor.getString(INDEX_MOVIE_INFO_BACKDROP_PATH);
        Integer budget = cursor.getInt(INDEX_MOVIE_INFO_BUDGET);
        String homepage = cursor.getString(INDEX_MOVIE_INFO_HOMEPAGE);
        String imdbId = cursor.getString(INDEX_MOVIE_INFO_IMDB_ID);
        String originalLanguage = cursor.getString(INDEX_MOVIE_INFO_ORIGINAL_LANGUAGE);
        String originalTitle = cursor.getString(INDEX_MOVIE_INFO_ORIGINAL_TITLE);
        String overview = cursor.getString(INDEX_MOVIE_INFO_OVERVIEW);
        Double popularity = cursor.getDouble(INDEX_MOVIE_INFO_POPULARITY);
        String posterPath = cursor.getString(INDEX_MOVIE_INFO_POSTER_PATH);
        String releaseDate = cursor.getString(INDEX_MOVIE_INFO_RELEASE_DATE);
        Integer revenue = cursor.getInt(INDEX_MOVIE_INFO_REVENUE);
        Integer runtime = cursor.getInt(INDEX_MOVIE_INFO_RUNTIME);
        String status = cursor.getString(INDEX_MOVIE_INFO_STATUS);
        String tagline = cursor.getString(INDEX_MOVIE_INFO_TAGLINE);
        String title = cursor.getString(INDEX_MOVIE_INFO_TITLE);
        Boolean video = cursor.getInt(INDEX_MOVIE_INFO_VIDEO) == 1;
        Double voteAverage = cursor.getDouble(INDEX_MOVIE_INFO_VOTE_AVERAGE);
        Integer voteCount = cursor.getInt(INDEX_MOVIE_INFO_VOTE_COUNT);

        MovieDetailedInfo movieDetailedInfo = new MovieDetailedInfo();

        Reviews reviews = ReviewsHelper.getReviewsFromFromMovieId(context, id);;
        movieDetailedInfo.setReviews(reviews);
        Videos videos = VideosHelper.getVideosFromFromMovieId(context, id);
        movieDetailedInfo.setVideos(videos);

        if(cursor.getInt(INDEX_MOVIE_INFO_GENRES) != -1) {
            List<Genre> genres = GenresHelper.getGenresFromMovieId(context, id);
            movieDetailedInfo.setGenres(genres);
        }
        if(cursor.getInt(INDEX_MOVIE_INFO_BELONGS_TO_COLLECTION) != -1) {
            MovieCollection movieCollection = MovieCollectionHelper.getMovieCollectionFromMovieId(context, id);
            movieDetailedInfo.setBelongsToCollection(movieCollection);
        }
        if(cursor.getInt(INDEX_MOVIE_INFO_PRODUCTION_COMPANIES) != -1) {
            List<ProductionCompanies> productionCompanies = ProductionCompaniesHelper.getProductionCompaniesFromMovieId(context, id);
            movieDetailedInfo.setProductionCompanies(productionCompanies);
        }
        if(cursor.getInt(INDEX_MOVIE_INFO_PRODUCTION_COUNTRIES) != -1) {
            List<ProductionCountries> productionCountries = ProductionCountriesHelper.getProductionCountriesFromMovieId(context, id);
            movieDetailedInfo.setProductionCountries(productionCountries);
        }
        if(cursor.getInt(INDEX_MOVIE_INFO_SPOKEN_LANGUAGES) != -1) {
            List<SpokenLanguages> spokenLanguages = SpokenLanguagesHelper.getSpokenLanguagesFromMovieId(context, id);
            movieDetailedInfo.setSpokenLanguages(spokenLanguages);
        }

        movieDetailedInfo.setAdult(adult);
        movieDetailedInfo.setBackdropPath(backdropPath);
        movieDetailedInfo.setBudget(budget);
        movieDetailedInfo.setHomepage(homepage);
        movieDetailedInfo.setId(id);
        movieDetailedInfo.setImdbId(imdbId);
        movieDetailedInfo.setOriginalLanguage(originalLanguage);
        movieDetailedInfo.setOriginalTitle(originalTitle);
        movieDetailedInfo.setOverview(overview);
        movieDetailedInfo.setPopularity(popularity);
        movieDetailedInfo.setPosterPath(posterPath);
        movieDetailedInfo.setReleaseDate(releaseDate);
        movieDetailedInfo.setRevenue(revenue);
        movieDetailedInfo.setRuntime(runtime);
        movieDetailedInfo.setStatus(status);
        movieDetailedInfo.setTagline(tagline);
        movieDetailedInfo.setTitle(title);
        movieDetailedInfo.setVideo(video);
        movieDetailedInfo.setVoteAverage(voteAverage);
        movieDetailedInfo.setVoteCount(voteCount);

        return movieDetailedInfo;
    }
}
