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

package com.example.android.popularmovies.utilities;

import android.content.res.Resources;
import android.util.Log;

import com.example.android.popularmovies.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Utility class for reading the file with the api-key
 *
 * @author Julia Mattjus
 */
public class ApiKeyUtility {

    private static final String TAG = ApiKeyUtility.class.getSimpleName();

    /**
     * Read the API-key specified in the file res/raw/the_movie_db.txt
     * @param res Reference to the resources
     * @return A string containing what is written to the file
     */
    public static String readApiKey(Resources res) {
        InputStream is = res.openRawResource(R.raw.the_movie_db);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String apiKey = br.readLine();
            Log.d(TAG, "apiKey: " + apiKey);
            return apiKey;
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Unable to read API file the_movie_db.txt", e);
            return "";
        } catch (IOException e) {
            Log.e(TAG, "Unable to read API file the_movie_db.txt", e);
            return "";
        }
    }
}
