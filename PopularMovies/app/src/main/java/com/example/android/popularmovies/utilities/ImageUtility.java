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

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Utility class for writing and reading images from the local storage, this is used for storing
 * the images for movies that are set as favourite so that they're accessible when offline.
 *
 * @author Julia Mattjus
 */
public class ImageUtility {

    private static final String TAG = ImageUtility.class.getSimpleName();

    /**
     * Helper method for checking whether or not a file is in the internal storage
     * @param context
     * @param filename
     * @return
     */
    public static boolean hasLocalImageFile(Context context, String filename) {
        final String filenameCorrected = filename.replace("/", "");
        File f = getFileFromPath(context, filenameCorrected);
        if(f.exists() && !f.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper method for getting a file from the internal storage by its filename
     *
     * @param context
     * @param filename
     * @return
     */
    public static File getFileFromPath(Context context, String filename) {
        final String filenameCorrected = filename.replace("/", "");
        Log.d(TAG, "getFileFromPath - context.getFilesDir(): " + context.getFilesDir().getAbsolutePath());
        return new File(context.getFilesDir(), filenameCorrected);
    }

    /**
     * Method for saving an image to the internal storage
     *
     * @param context
     * @param bitmap
     * @param filename
     */
    public static void saveImage(final Context context, final Bitmap bitmap, final String filename) {
        final String imagePath = filename.replace("/", "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = getFileFromPath(context, imagePath);

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }).start();
    }

    /**
     * Method for removing an image from the internal storage
     *
     * @param context
     * @param filename
     */
    public static void removeImage(Context context, String filename) {
        String imagePath = filename.replace("/", "");
        if(hasLocalImageFile(context, imagePath)) {
            context.deleteFile(imagePath);
        }
    }
}
