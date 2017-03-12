package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author Julia Mattjus
 */
public class ImageUtility {

    private static final String TAG = ImageUtility.class.getSimpleName();

    public static boolean hasLocalImageFile(Context context, String filename) {
        final String filenameCorrected = filename.replace("/", "");
        File f = getFileFromPath(context, filenameCorrected);
        if(f.exists() && !f.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    public static File getFileFromPath(Context context, String filename) {
        final String filenameCorrected = filename.replace("/", "");
        Log.d(TAG, "getFileFromPath - context.getFilesDir(): " + context.getFilesDir().getAbsolutePath());
        return new File(context.getFilesDir(), filenameCorrected);
    }

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

    public static void removeImage(Context context, String filename) {
        String imagePath = filename.replace("/", "");
        if(hasLocalImageFile(context, imagePath)) {
            context.deleteFile(imagePath);
        }
    }
}
