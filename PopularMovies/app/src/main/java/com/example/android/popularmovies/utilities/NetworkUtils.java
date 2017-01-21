/*
 * Copyright 2017 Julia Mattjus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

/**
 * Created by Julia on 2017-01-21.
 */

public class NetworkUtils {
    public enum Sorting {
        POPULAR, TOP_RATED
    }

    private static final String TAG = NetworkUtils.class.getSimpleName();

    //private static final String THE_MOVIE_DB_URL = "http://image.tmdb.org/t/p/";

    private static final String THE_MOVIE_DB_URL = "http://api.themoviedb.org/3/movie/";
    private static final String THE_MOVIE_DB_POSTER_URL = "http://image.tmdb.org/t/p/";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String API_KEY = "api_key";
    private static final String W92 = "w92";
    private static final String W154 = "w154";
    private static final String W185 = "w185";
    private static final String W342 = "w342";
    private static final String W500 = "w500";
    private static final String W780 = "w780";
    private static final String SEPARATOR = "/";

    private static final String format = "json";

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

        Log.v(TAG, "build URI: " + url);

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

    public static String buildPosterUrl(String posterPath) {
        return THE_MOVIE_DB_POSTER_URL + W342 + SEPARATOR + posterPath;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
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

