package com.example.android.popularmovies.data.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Reviews;
import com.example.android.popularmovies.data.db.ReviewsProvider;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

/**
 * @author Julia Mattjus
 */
@AllArgsConstructor
public class ReviewsLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context mContext;
    private MovieLoader movieLoader;
    private List<Integer> reviewIds;

    private static final String TAG = ReviewsLoader.class.getSimpleName();

    private static final int ID_REVIEWS_LOADER = 354;

    public static final int INDEX_REVIEW_ID = 0;
    public static final int INDEX_REVIEW_AUTHOR = 1;
    public static final int INDEX_REVIEW_CONTENT = 2;
    public static final int INDEX_REVIEW_ISO_639_1 = 3;
    public static final int INDEX_REVIEW_MEDIA_ID = 4;
    public static final int INDEX_REVIEW_MEDIA_TITLE = 5;
    public static final int INDEX_REVIEW_MEDIA_TYPE = 6;
    public static final int INDEX_REVIEW_URL = 7;

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        List<String> whereArgs = new ArrayList<String>();
        for(Integer reviewId : reviewIds) {
            whereArgs.add(reviewId.toString());
        }

        return new CursorLoader(mContext,
                ReviewsProvider.Reviews.REVIEWS,
                null,
                "_id = ?",
                whereArgs.toArray(new String[reviewIds.size()]),
                null);
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

        List<Review> reviewList = new ArrayList<Review>();
        while(data.moveToNext()) {
            int id = data.getInt(INDEX_REVIEW_ID);
            String author = data.getString(INDEX_REVIEW_AUTHOR);
            String content = data.getString(INDEX_REVIEW_CONTENT);
            String iso6391 = data.getString(INDEX_REVIEW_ISO_639_1);
            String mediaId = data.getString(INDEX_REVIEW_MEDIA_ID);
            String mediaTitle = data.getString(INDEX_REVIEW_MEDIA_TITLE);
            String mediaType = data.getString(INDEX_REVIEW_MEDIA_TYPE);
            String reviewUrl = data.getString(INDEX_REVIEW_URL);

            reviewList.add(new Review(id,
                    author,
                    content,
                    iso6391,
                    mediaId,
                    mediaTitle,
                    mediaType,
                    reviewUrl));
        }

        Reviews reviews = new Reviews(1, reviewList, 1, reviewList.size());

        movieLoader.setReviews(reviews);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
