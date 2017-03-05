package com.example.android.popularmovies.data.db.movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.example.android.popularmovies.data.Genre;
import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.MovieCollection;
import com.example.android.popularmovies.data.MovieDetailedInfo;
import com.example.android.popularmovies.data.ProductionCompanies;
import com.example.android.popularmovies.data.ProductionCountries;
import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Reviews;
import com.example.android.popularmovies.data.SpokenLanguages;
import com.example.android.popularmovies.data.Video;
import com.example.android.popularmovies.data.Videos;
import com.example.android.popularmovies.data.db.genres.GenresProvider;
import com.example.android.popularmovies.data.db.moviecollection.MovieCollectionProvider;
import com.example.android.popularmovies.data.db.productioncompanies.ProductionCompaniesProvider;
import com.example.android.popularmovies.data.db.productioncountries.ProductionCountriesProvider;
import com.example.android.popularmovies.data.db.reviews.ReviewsProvider;
import com.example.android.popularmovies.data.db.spokenlanguages.SpokenLanguagesProvider;
import com.example.android.popularmovies.data.db.videos.VideosProvider;
import com.example.android.popularmovies.utilities.JsonUtility;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julia Mattjus
 */
@ContentProvider(authority = MovieDetailedInfosProvider.AUTHORITY, database = MovieDetailedInfosDatabase.class)
public final class MovieDetailedInfosProvider {

    @TableEndpoint(table = MovieDetailedInfosDatabase.MOVIE_DETAILED_INFOS) public static class MovieDetailedInfos {

        @ContentUri(
                path = "movie_detailed_infos",
                type = "vnd.android.cursor.dir/movie_detailed_infos",
                defaultSort = MovieDetailedInfosColumns._ID
        )
        public static final Uri MOVIE_DETAILED_INFOS = Uri.parse("content://" + AUTHORITY + "/movie_detailed_infos");
    }

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.movies.MovieDetailedInfosProvider";

    public static final int INDEX_MOVIE_INFO_ID = 0;
    public static final int INDEX_MOVIE_INFO_ADULT = 1;
    public static final int INDEX_MOVIE_INFO_BACKDROP_PATH = 2;
    public static final int INDEX_MOVIE_INFO_BELONGS_TO_COLLECTION = 3;
    public static final int INDEX_MOVIE_INFO_BUDGET = 4;
    public static final int INDEX_MOVIE_INFO_GENRES = 5;
    public static final int INDEX_MOVIE_INFO_HOMEPAGE = 6;
    public static final int INDEX_MOVIE_INFO_IMDB_ID = 7;
    public static final int INDEX_MOVIE_INFO_ORIGINAL_LANGUAGE = 8;
    public static final int INDEX_MOVIE_INFO_ORIGINAL_TITLE = 9;
    public static final int INDEX_MOVIE_INFO_OVERVIEW = 10;
    public static final int INDEX_MOVIE_INFO_POPULARITY = 11;
    public static final int INDEX_MOVIE_INFO_POSTER_PATH = 12;
    public static final int INDEX_MOVIE_INFO_PRODUCTION_COMPANIES = 13;
    public static final int INDEX_MOVIE_INFO_PRODUCTION_COUNTRIES = 14;
    public static final int INDEX_MOVIE_INFO_RELEASE_DATE = 15;
    public static final int INDEX_MOVIE_INFO_REVENUE = 16;
    public static final int INDEX_MOVIE_INFO_RUNTIME = 17;
    public static final int INDEX_MOVIE_INFO_SPOKEN_LANGUAGES = 18;
    public static final int INDEX_MOVIE_INFO_STATUS = 19;
    public static final int INDEX_MOVIE_INFO_TAGLINE = 20;
    public static final int INDEX_MOVIE_INFO_TITLE = 21;
    public static final int INDEX_MOVIE_INFO_VIDEO = 22;
    public static final int INDEX_MOVIE_INFO_VOTE_AVERAGE = 23;
    public static final int INDEX_MOVIE_INFO_VOTE_COUNT = 24;
    public static final int INDEX_MOVIE_INFO_VIDEOS = 25;
    public static final int INDEX_MOVIE_INFO_REVIEWS = 26;

    public static final int INDEX_MAIN_MOVIE_PROJECTION_ID = 0;
    public static final int INDEX_MAIN_MOVIE_PROJECTION_POSTER_PATH = 1;

    public static final String[] MAIN_MOVIES_PROJECTION = {
            MovieDetailedInfosColumns._ID,
            MovieDetailedInfosColumns.POSTER_PATH,
    };

    private static final String TAG = MovieDetailedInfosProvider.class.getSimpleName();

    public static void write(Context context, MovieDetailedInfo movieDetailedInfo) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieDetailedInfosColumns._ID, movieDetailedInfo.getId());
        contentValues.put(MovieDetailedInfosColumns.ADULT, movieDetailedInfo.getAdult() ? 1 : 0);
        contentValues.put(MovieDetailedInfosColumns.BACKDROP_PATH, movieDetailedInfo.getBackdropPath());
        contentValues.put(MovieDetailedInfosColumns.BUDGET, movieDetailedInfo.getBudget());
        contentValues.put(MovieDetailedInfosColumns.HOMEPAGE, movieDetailedInfo.getHomepage());
        contentValues.put(MovieDetailedInfosColumns.IMDB_ID, movieDetailedInfo.getImdbId());
        contentValues.put(MovieDetailedInfosColumns.ORIGINAL_LANGUAGE, movieDetailedInfo.getOriginalLanguage());
        contentValues.put(MovieDetailedInfosColumns.ORIGINAL_TITLE, movieDetailedInfo.getOriginalTitle());
        contentValues.put(MovieDetailedInfosColumns.OVERVIEW, movieDetailedInfo.getOverview());
        contentValues.put(MovieDetailedInfosColumns.POPULARITY, movieDetailedInfo.getPopularity());
        contentValues.put(MovieDetailedInfosColumns.POSTER_PATH, movieDetailedInfo.getPosterPath());
        contentValues.put(MovieDetailedInfosColumns.HOMEPAGE, movieDetailedInfo.getHomepage());
        contentValues.put(MovieDetailedInfosColumns.IMDB_ID, movieDetailedInfo.getImdbId());
        contentValues.put(MovieDetailedInfosColumns.RELEASE_DATE, movieDetailedInfo.getReleaseDate());
        contentValues.put(MovieDetailedInfosColumns.REVENUE, movieDetailedInfo.getRevenue());

        List<String> reviewIds = new ArrayList<>();
        for(Review review : movieDetailedInfo.getReviews().getResults()) {
            reviewIds.add(review.getId());
        }
        contentValues.put(MovieDetailedInfosColumns.REVIEWS, JsonUtility.toJson(reviewIds));

        contentValues.put(MovieDetailedInfosColumns.RUNTIME, movieDetailedInfo.getRuntime());
        contentValues.put(MovieDetailedInfosColumns.STATUS, movieDetailedInfo.getStatus());
        contentValues.put(MovieDetailedInfosColumns.TAGLINE, movieDetailedInfo.getTagline());
        contentValues.put(MovieDetailedInfosColumns.TITLE, movieDetailedInfo.getTitle());
        contentValues.put(MovieDetailedInfosColumns.VIDEO, movieDetailedInfo.getVideo() ? 1 : 0);

        List<String> videoIds = new ArrayList<>();
        for(Video video : movieDetailedInfo.getVideos().getResults()) {
            videoIds.add(video.getId());
        }
        contentValues.put(MovieDetailedInfosColumns.VIDEOS, JsonUtility.toJson(videoIds));

        contentValues.put(MovieDetailedInfosColumns.VOTE_AVERAGE, movieDetailedInfo.getVoteAverage());
        contentValues.put(MovieDetailedInfosColumns.VOTE_COUNT, movieDetailedInfo.getVoteCount());

        if(movieDetailedInfo.getGenres() != null) {
            GenresProvider.write(context, movieDetailedInfo.getId(), movieDetailedInfo.getGenres());
            contentValues.put(MovieDetailedInfosColumns.GENRES, movieDetailedInfo.getId());
        } else {
            contentValues.put(MovieDetailedInfosColumns.GENRES, -1);
        }

        if(movieDetailedInfo.getBelongsToCollection() != null) {
            MovieCollectionProvider.write(context, movieDetailedInfo.getId(), movieDetailedInfo.getBelongsToCollection());
            contentValues.put(MovieDetailedInfosColumns.BELONGS_TO_COLLECTION, movieDetailedInfo.getId());
        } else {
            contentValues.put(MovieDetailedInfosColumns.BELONGS_TO_COLLECTION, -1);
        }

        if(movieDetailedInfo.getProductionCompanies() != null) {
            ProductionCompaniesProvider.write(context, movieDetailedInfo.getId(), movieDetailedInfo.getProductionCompanies());
            contentValues.put(MovieDetailedInfosColumns.PRODUCTION_COMPANIES, movieDetailedInfo.getId());
        } else {
            contentValues.put(MovieDetailedInfosColumns.PRODUCTION_COMPANIES, -1);
        }

        if(movieDetailedInfo.getProductionCountries() != null) {
            ProductionCountriesProvider.write(context, movieDetailedInfo.getId(), movieDetailedInfo.getProductionCountries());
            contentValues.put(MovieDetailedInfosColumns.PRODUCTION_COUNTRIES, movieDetailedInfo.getId());
        } else {
            contentValues.put(MovieDetailedInfosColumns.PRODUCTION_COUNTRIES, -1);
        }

        if(movieDetailedInfo.getSpokenLanguages() != null) {
            SpokenLanguagesProvider.write(context, movieDetailedInfo.getId(), movieDetailedInfo.getSpokenLanguages());
            contentValues.put(MovieDetailedInfosColumns.SPOKEN_LANGUAGES, movieDetailedInfo.getId());
        } else {
            contentValues.put(MovieDetailedInfosColumns.SPOKEN_LANGUAGES, -1);
        }

        ReviewsProvider.write(context, movieDetailedInfo.getReviews());
        VideosProvider.write(context, movieDetailedInfo.getVideos());

        context.getContentResolver().insert(MovieDetailedInfosProvider.MovieDetailedInfos.MOVIE_DETAILED_INFOS, contentValues);
    }

    public static Boolean isFavourite(Context context, Integer movieId) {
        Cursor cursor = context.getContentResolver().query(
                MovieDetailedInfosProvider.MovieDetailedInfos.MOVIE_DETAILED_INFOS,
                null,
                MovieDetailedInfosColumns._ID + " = ?",
                new String[] { movieId.toString() },
                null);

        Boolean isFavourite = cursor != null && cursor.moveToFirst();
        Log.d(TAG, "isFavourite: " + isFavourite.toString());
        return isFavourite;
    }

    public static Cursor getFavouritesCursor(Context context) {
        String sortOrder = MovieDetailedInfosColumns.TITLE + " ASC";

        Cursor cursor = context.getContentResolver().query(
                MovieDetailedInfosProvider.MovieDetailedInfos.MOVIE_DETAILED_INFOS,
                MAIN_MOVIES_PROJECTION,
                null,
                null,
                sortOrder);

        if(cursor == null) {
            return null;
        }

        return cursor;
    }

    public static Integer getMovieIdFromMainProjectionCursor(Cursor cursor) {
        return cursor.getInt(INDEX_MAIN_MOVIE_PROJECTION_ID);
    }

    public static String getPosterPathFromMainProjectionCursor(Cursor cursor) {
        return cursor.getString(INDEX_MAIN_MOVIE_PROJECTION_POSTER_PATH);
    }

    public static List<Movie> getFavourites(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MovieDetailedInfosProvider.MovieDetailedInfos.MOVIE_DETAILED_INFOS,
                null,
                null,
                null,
                null);

        if(cursor == null || !cursor.moveToFirst()) {
            return null;
        }

        List<Movie> favouriteMovies = new ArrayList<>();

        favouriteMovies.add(getMovieFromCursor(context, cursor));

        while(cursor.moveToNext()) {
            favouriteMovies.add(getMovieFromCursor(context, cursor));
        }

        return favouriteMovies;
    }

    public static MovieDetailedInfo getFavourite(Context context, Integer movieId) {

        Log.d(TAG, "getFavourite - movieId: " + movieId);

        Cursor cursor = context.getContentResolver().query(
                MovieDetailedInfosProvider.MovieDetailedInfos.MOVIE_DETAILED_INFOS,
                null,
                MovieDetailedInfosColumns._ID + " = ?",
                new String[] { movieId.toString() },
                null);

        if(cursor == null || !cursor.moveToFirst()) {
            return null;
        }

        return getDetailedMovieFromCursor(context, cursor);
    }

    public static void removeFavourite(Context context, MovieDetailedInfo movieDetailedInfo) {
        remove(context, movieDetailedInfo.getId());

        GenresProvider.remove(context, movieDetailedInfo.getId());
        MovieCollectionProvider.remove(context, movieDetailedInfo.getId());
        ProductionCompaniesProvider.remove(context, movieDetailedInfo.getId());
        ProductionCountriesProvider.remove(context, movieDetailedInfo.getId());
        SpokenLanguagesProvider.remove(context, movieDetailedInfo.getId());

        List<String> videosIds = new ArrayList<>();
        for(Video video : movieDetailedInfo.getVideos().getResults()) {
            videosIds.add(video.getId());
        }
        VideosProvider.remove(context, videosIds);

        List<String> reviewIds = new ArrayList<>();
        for(Review review : movieDetailedInfo.getReviews().getResults()) {
            reviewIds.add(review.getId());
        }
        ReviewsProvider.remove(context, reviewIds);
    }

    private static int remove(Context context, Integer movieId) {
        return context.getContentResolver().delete(
                MovieDetailedInfosProvider.MovieDetailedInfos.MOVIE_DETAILED_INFOS,
                MovieDetailedInfosColumns._ID + " = ?",
                new String[] { movieId.toString() });
    }

    public static Movie getMovieFromCursor(Context context, Cursor cursor) {

        Integer id = cursor.getInt(INDEX_MOVIE_INFO_ID);
        Boolean adult = cursor.getInt(INDEX_MOVIE_INFO_ADULT) == 1 ? true : false;
        String backdropPath = cursor.getString(INDEX_MOVIE_INFO_BACKDROP_PATH);
        String originalLanguage = cursor.getString(INDEX_MOVIE_INFO_ORIGINAL_LANGUAGE);
        String originalTitle = cursor.getString(INDEX_MOVIE_INFO_ORIGINAL_TITLE);
        String overview = cursor.getString(INDEX_MOVIE_INFO_OVERVIEW);
        Double popularity = cursor.getDouble(INDEX_MOVIE_INFO_POPULARITY);
        String posterPath = cursor.getString(INDEX_MOVIE_INFO_POSTER_PATH);
        String releaseDate = cursor.getString(INDEX_MOVIE_INFO_RELEASE_DATE);
        String title = cursor.getString(INDEX_MOVIE_INFO_TITLE);
        Boolean video = cursor.getInt(INDEX_MOVIE_INFO_VIDEO) == 1 ? true : false;
        Double voteAverage = cursor.getDouble(INDEX_MOVIE_INFO_VOTE_AVERAGE);
        Integer voteCount = cursor.getInt(INDEX_MOVIE_INFO_VOTE_COUNT);

        List<Genre> genres = GenresProvider.getGenresFromMovieId(context, id);
        Integer[] genreIds = new Integer[genres.size()];
        for(int i = 0; i < genres.size(); i++) {
            genreIds[i] = genres.get(i).getId();
        }

        Movie movie = new Movie(posterPath, adult, overview, releaseDate, genreIds, id, originalTitle,
                originalLanguage, title, backdropPath, popularity, voteCount, video, voteAverage);

        return movie;
    }

    private static MovieDetailedInfo getDetailedMovieFromCursor(Context context, Cursor cursor) {
        Integer id = cursor.getInt(INDEX_MOVIE_INFO_ID);
        Boolean adult = cursor.getInt(INDEX_MOVIE_INFO_ADULT) == 1 ? true : false;
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
        Boolean video = cursor.getInt(INDEX_MOVIE_INFO_VIDEO) == 1 ? true : false;
        Double voteAverage = cursor.getDouble(INDEX_MOVIE_INFO_VOTE_AVERAGE);
        Integer voteCount = cursor.getInt(INDEX_MOVIE_INFO_VOTE_COUNT);

        List<String> videosIds =
                JsonUtility.fromJson(cursor.getString(INDEX_MOVIE_INFO_VIDEOS),
                        (new ArrayList<String>()).getClass());
        List<String> reviewsIds =
                JsonUtility.fromJson(cursor.getString(INDEX_MOVIE_INFO_REVIEWS),
                        (new ArrayList<String>()).getClass());

        MovieDetailedInfo movieDetailedInfo = new MovieDetailedInfo();
        if(cursor.getInt(INDEX_MOVIE_INFO_GENRES) != -1) {
            List<Genre> genres = GenresProvider.getGenresFromMovieId(context, id);
            movieDetailedInfo.setGenres(genres);
        }
        if(cursor.getInt(INDEX_MOVIE_INFO_BELONGS_TO_COLLECTION) != -1) {
            MovieCollection movieCollection = MovieCollectionProvider.getMovieCollectionFromMovieId(context, id);
            movieDetailedInfo.setBelongsToCollection(movieCollection);
        }
        if(cursor.getInt(INDEX_MOVIE_INFO_PRODUCTION_COMPANIES) != -1) {
            List<ProductionCompanies> productionCompanies = ProductionCompaniesProvider.getProductionCompaniesFromMovieId(context, id);
            movieDetailedInfo.setProductionCompanies(productionCompanies);
        }
        if(cursor.getInt(INDEX_MOVIE_INFO_PRODUCTION_COUNTRIES) != -1) {
            List<ProductionCountries> productionCountries = ProductionCountriesProvider.getProductionCountriesFromMovieId(context, id);
            movieDetailedInfo.setProductionCountries(productionCountries);
        }
        if(cursor.getInt(INDEX_MOVIE_INFO_SPOKEN_LANGUAGES) != -1) {
            List<SpokenLanguages> spokenLanguages = SpokenLanguagesProvider.getSpokenLanguagesFromMovieId(context, id);
            movieDetailedInfo.setSpokenLanguages(spokenLanguages);
        }
        Reviews reviews = ReviewsProvider.getReviewsFromFromIds(context, reviewsIds);
        Videos videos = VideosProvider.getVideosFromFromIds(context, videosIds);

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
        movieDetailedInfo.setVideos(videos);
        movieDetailedInfo.setReviews(reviews);

        return movieDetailedInfo;
    }
}
