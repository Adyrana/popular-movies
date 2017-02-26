/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import lombok.Getter;

/**
 * Network utility class for building urls and making requests.
 *
 * @author Julia Mattjus
 */
public class NetworkUtils {
    public enum Sorting {
        POPULAR, TOP_RATED
    }

    public enum ImageQuality {
        W92("w92"), W154("w154"), W185("w185"), W342("w342"), W500("w500"), W780("w780");

        private @Getter String quality;

        ImageQuality(String quality) {
            this.quality = quality;
        }
    }

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String THE_MOVIE_DB_URL = "http://api.themoviedb.org/3/movie/";
    private static final String THE_MOVIE_DB_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String API_KEY = "api_key";
    private static final String SEPARATOR = "/";
    private static final String APPEND_TO_RESPONSE = "append_to_response";
    private static final String VIDEOS = "videos";
    private static final String REVIEWS = "reviews";

    private static final String format = "json";

    /**
     * Build an URL for requests for popular or top rated movies
     *
     * @param sorting The sorting to use
     * @param apiKey Api-key towards The Movie DB
     * @return The URL to use to query The Movie DB
     */
    public static URL buildUrl(Sorting sorting, String apiKey) {
        Uri builtUri = Uri.parse(THE_MOVIE_DB_URL).buildUpon()
                .appendPath(getSorting(sorting))
                .appendQueryParameter(API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        Log.v(TAG, "buildUrl - build URI: " + url);

        return url;
    }

    /**
     * Buid an URL for requesting data for a specific movie by its id.
     *
     * @param movieId Id of the movie to request data for
     * @param apiKey Api-key towards The Movie DB
     * @return The URL to use to query The Movie DB
     */
    public static URL buildMovieURL(Integer movieId, String apiKey) {
        Uri builtUri = Uri.parse(THE_MOVIE_DB_URL).buildUpon()
                .appendPath(movieId.toString())
                .appendQueryParameter(API_KEY, apiKey)
                .appendQueryParameter(APPEND_TO_RESPONSE, VIDEOS + "," + REVIEWS)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        Log.v(TAG, "buildMovieURL - build URI: " + url);

        return url;
    }

    private static String getSorting(Sorting sorting) {
        switch(sorting) {
            case POPULAR:
                return POPULAR;
            case TOP_RATED:
                return TOP_RATED;
            default:
                return POPULAR;
        }
    }

    /**
     * Build an URL as a String to request posters from The Movie DB with
     *
     * @param posterPath Path to the poster
     * @return The URL as a String to use to get The Movie DB posters with
     */
    public static String buildImageUrl(String posterPath, ImageQuality imageQuality) {
        String url = THE_MOVIE_DB_IMAGE_URL + imageQuality.getQuality() + SEPARATOR + posterPath;
        Log.d(TAG, "buildImageUrl: " + url);
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Log.v(TAG, "opening connection to: " + url.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

