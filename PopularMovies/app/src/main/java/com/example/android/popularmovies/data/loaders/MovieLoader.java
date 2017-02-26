package com.example.android.popularmovies.data.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.android.popularmovies.data.MovieDetailedInfo;
import com.example.android.popularmovies.data.Reviews;
import com.example.android.popularmovies.data.Videos;
import com.example.android.popularmovies.data.db.MovieDetailedInfosProvider;
import com.example.android.popularmovies.data.db.ReviewsProvider;
import com.example.android.popularmovies.utilities.JsonUtility;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

/**
 * @author Julia Mattjus
 */
@AllArgsConstructor
public class MovieLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private class MovieDbInfo {
        int id;
        int adult;
        String backdropPath;
        int belongsToCollection;
        int budget;
        String genreIdsJson;
        String homepage;
        String imdbId;
        String originalLanguage;
        String originalTitle;
        String overview;
        double popularity;
        String posterPath;
        String productionCompanieIdsJson;
        String productionCountrieIdsJson;
        String releaseDate;
        int revenue;
        int runtime;
        String spokenLanguageIdsJson;
        String status;
        String tagline;
        String title;
        int video;
        double voteAverage;
        int voteCount;
        String videoIdsJson;
        String reviewIdsJson;
        Videos videos;
        Reviews reviews;
    }

    private Integer movieId;
    private List<Integer> reviewIds;
    private List<Integer> videoIds;
    private Context mContext;
    private MovieDbInfo movieDbInfo = new MovieDbInfo();

    private static final String TAG = MovieLoader.class.getSimpleName();

    private static final int ID_DETAIL_LOADER = 353;

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

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        return new AsyncTaskLoader<Cursor>(mContext) {

            Cursor mMovieDetailedInfoData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieDetailedInfoData != null) {
                    deliverResult(mMovieDetailedInfoData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return mContext.getContentResolver().query(MovieDetailedInfosProvider.MovieDetailedInfos.MOVIE_DETAILED_INFOS,
                            null,
                            "_id = ?",
                            new String[] {movieId.toString() },
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mMovieDetailedInfoData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }

        movieDbInfo.id = data.getInt(INDEX_MOVIE_INFO_ID);
        movieDbInfo.adult = data.getInt(INDEX_MOVIE_INFO_ADULT);
        movieDbInfo.backdropPath = data.getString(INDEX_MOVIE_INFO_BACKDROP_PATH);
        movieDbInfo.belongsToCollection = data.getInt(INDEX_MOVIE_INFO_BELONGS_TO_COLLECTION);
        movieDbInfo.budget = data.getInt(INDEX_MOVIE_INFO_BUDGET);
        movieDbInfo.genreIdsJson = data.getString(INDEX_MOVIE_INFO_GENRES);
        movieDbInfo.homepage = data.getString(INDEX_MOVIE_INFO_HOMEPAGE);
        movieDbInfo.imdbId = data.getString(INDEX_MOVIE_INFO_IMDB_ID);
        movieDbInfo.originalLanguage = data.getString(INDEX_MOVIE_INFO_ORIGINAL_LANGUAGE);
        movieDbInfo.originalTitle = data.getString(INDEX_MOVIE_INFO_ORIGINAL_TITLE);
        movieDbInfo.overview = data.getString(INDEX_MOVIE_INFO_OVERVIEW);
        movieDbInfo.popularity = data.getDouble(INDEX_MOVIE_INFO_POPULARITY);
        movieDbInfo.posterPath = data.getString(INDEX_MOVIE_INFO_POSTER_PATH);
        movieDbInfo.productionCompanieIdsJson = data.getString(INDEX_MOVIE_INFO_PRODUCTION_COMPANIES);
        movieDbInfo.productionCountrieIdsJson = data.getString(INDEX_MOVIE_INFO_PRODUCTION_COUNTRIES);
        movieDbInfo.releaseDate = data.getString(INDEX_MOVIE_INFO_RELEASE_DATE);
        movieDbInfo.revenue = data.getInt(INDEX_MOVIE_INFO_REVENUE);
        movieDbInfo.runtime = data.getInt(INDEX_MOVIE_INFO_RUNTIME);
        movieDbInfo.spokenLanguageIdsJson = data.getString(INDEX_MOVIE_INFO_SPOKEN_LANGUAGES);
        movieDbInfo.status = data.getString(INDEX_MOVIE_INFO_STATUS);
        movieDbInfo.tagline = data.getString(INDEX_MOVIE_INFO_TAGLINE);
        movieDbInfo.title = data.getString(INDEX_MOVIE_INFO_TITLE);
        movieDbInfo.video = data.getInt(INDEX_MOVIE_INFO_VIDEO);
        movieDbInfo.voteAverage = data.getDouble(INDEX_MOVIE_INFO_VOTE_AVERAGE);
        movieDbInfo.voteCount = data.getInt(INDEX_MOVIE_INFO_VOTE_COUNT);
        movieDbInfo.videoIdsJson = data.getString(INDEX_MOVIE_INFO_VIDEOS);
        movieDbInfo.reviewIdsJson = data.getString(INDEX_MOVIE_INFO_REVIEWS);

        List<Integer> genreIds = JsonUtility.fromJson(movieDbInfo.genreIdsJson, (new ArrayList<Integer>()).getClass());
        List<Integer> productionCompanieIds = JsonUtility.fromJson(movieDbInfo.productionCompanieIdsJson, (new ArrayList<Integer>()).getClass());
        List<Integer> productionCountrieIds = JsonUtility.fromJson(movieDbInfo.productionCountrieIdsJson, (new ArrayList<Integer>()).getClass());
        List<Integer> spokenLanguageIds = JsonUtility.fromJson(movieDbInfo.spokenLanguageIdsJson, (new ArrayList<Integer>()).getClass());
        List<Integer> videoIds = JsonUtility.fromJson(movieDbInfo.videoIdsJson, (new ArrayList<Integer>()).getClass());
        List<Integer> reviewIds = JsonUtility.fromJson(movieDbInfo.reviewIdsJson, (new ArrayList<Integer>()).getClass());


        new VideosLoader(mContext, this, videoIds);
        new ReviewsLoader(mContext, this, reviewIds);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    private MovieDetailedInfo build() {
        MovieDetailedInfo movieDetailedInfo = new MovieDetailedInfo();
        movieDetailedInfo.setAdult(movieDbInfo.adult == 1 ? true : false);
        movieDetailedInfo.setBackdropPath(movieDbInfo.backdropPath);
        //movieDetailedInfo.setBelongsToCollection(movieDbInfo.belongsToCollection);
        movieDetailedInfo.setBudget(movieDbInfo.budget);
        //movieDetailedInfo.setBackdropPath(movieDbInfo.genres);
        movieDetailedInfo.setHomepage(movieDbInfo.homepage);
        movieDetailedInfo.setId(movieDbInfo.id);
        movieDetailedInfo.setImdbId(movieDbInfo.imdbId);
        movieDetailedInfo.setOriginalLanguage(movieDbInfo.originalLanguage);
        movieDetailedInfo.setOriginalTitle(movieDbInfo.originalTitle);
        movieDetailedInfo.setOverview(movieDbInfo.overview);
        movieDetailedInfo.setPopularity(movieDbInfo.popularity);
        movieDetailedInfo.setPosterPath(movieDbInfo.posterPath);
        //movieDetailedInfo.setBackdropPath(movieDbInfo.productionCompanies);
        //movieDetailedInfo.setBackdropPath(movieDbInfo.productionCountries);
        movieDetailedInfo.setReleaseDate(movieDbInfo.releaseDate);
        movieDetailedInfo.setRevenue(movieDbInfo.revenue);
        movieDetailedInfo.setRuntime(movieDbInfo.runtime);
        //movieDetailedInfo.setBackdropPath(movieDbInfo.spokenLanguages);
        movieDetailedInfo.setStatus(movieDbInfo.status);
        movieDetailedInfo.setTagline(movieDbInfo.tagline);
        movieDetailedInfo.setTitle(movieDbInfo.title);
        movieDetailedInfo.setVideo(movieDbInfo.video == 1 ? true : false);
        movieDetailedInfo.setVoteAverage(movieDbInfo.voteAverage);
        movieDetailedInfo.setVoteCount(movieDbInfo.voteCount);
        movieDetailedInfo.setVideos(movieDbInfo.videos);
        movieDetailedInfo.setReviews(movieDbInfo.reviews);

        return movieDetailedInfo;
    }

    protected void setVideos(Videos videos) {
        this.movieDbInfo.videos = videos;
    }

    protected void setReviews(Reviews reviews) {
        this.movieDbInfo.reviews = reviews;
    }
}
