package com.example.android.popularmovies.data.db.reviews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.db.videos.VideosColumns;
import com.example.android.popularmovies.utilities.JsonUtility;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julia Mattjus
 */
@ContentProvider(authority = ReviewsProvider.AUTHORITY, database = ReviewsDatabase.class)
public final class ReviewsProvider {

    public static final String AUTHORITY = "com.example.android.popularmovies.data.db.reviews.ReviewsProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    @TableEndpoint(table = ReviewsDatabase.REVIEWS)
    public static class Reviews {

        @ContentUri(
                path = "reviews",
                type = "vnd.android.cursor.dir/reviews",
                defaultSort = ReviewsColumns._ID
        )
        public static final Uri REVIEWS = Uri.parse("content://" + AUTHORITY + "/reviews");
    }

    public static final int INDEX_ID = 0;
    public static final int INDEX_MOVIE_ID = 1;
    public static final int INDEX_REVIEW_ID = 2;
    public static final int INDEX_REVIEW_AUTHOR = 3;
    public static final int INDEX_REVIEW_CONTENT = 4;
    public static final int INDEX_REVIEW_ISO_639_1 = 5;
    public static final int INDEX_REVIEW_MEDIA_ID = 6;
    public static final int INDEX_REVIEW_MEDIA_TITLE = 7;
    public static final int INDEX_REVIEW_MEDIA_TYPE = 8;
    public static final int INDEX_REVIEW_URL = 9;

    private static final String TAG = ReviewsProvider.class.getSimpleName();

    /**
     * Method for writing a set of reviews to the database
     *
     * @param context
     * @param movieId
     * @param reviews
     */
    public static void write(Context context, Integer movieId, com.example.android.popularmovies.data.Reviews reviews) {

        Log.d(TAG, "write - movieId \"" + movieId + "\" genres \"" + JsonUtility.toJson(reviews) + "\"");

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        for (Review review : reviews.getResults()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ReviewsColumns.MOVIE_ID, movieId);
            contentValues.put(ReviewsColumns.REVIEW_ID, review.getId());
            contentValues.put(ReviewsColumns.AUTHOR, review.getAuthor());
            contentValues.put(ReviewsColumns.CONTENT, review.getContent());
            contentValues.put(ReviewsColumns.ISO_639_1, review.getIso6391());
            contentValues.put(ReviewsColumns.MEDIA_ID, review.getMediaId());
            contentValues.put(ReviewsColumns.MEDIA_TITLE, review.getMediaTitle());
            contentValues.put(ReviewsColumns.MEDIA_TYPE, review.getMediaType());
            contentValues.put(ReviewsColumns.URL, review.getUrl());
            contentValuesList.add(contentValues);
        }

        context.getContentResolver().bulkInsert(Reviews.REVIEWS, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
    }

    /**
     * Method for removing reviews for a movie by the movie id
     *
     * @param context
     * @param movieId
     */
    public static void remove(Context context, Integer movieId) {

        Log.d(TAG, "remove - movieId: " + movieId);

        if(movieId == null) {
            return;
        }

        context.getContentResolver().delete(
                Reviews.REVIEWS,
                ReviewsColumns.MOVIE_ID + " = ?",
                new String[] { movieId.toString() });
    }

    /**
     * Method for getting reviews for a movie by the movie id
     *
     * @param context
     * @param movieId
     * @return
     */
    public static com.example.android.popularmovies.data.Reviews getReviewsFromFromMovieId(Context context, Integer movieId) {

        Log.d(TAG, "getReviewsFromFromMovieId - movieId: " + movieId);
        List<Review> reviewsList = new ArrayList<>();

        if(movieId == null) {
            return new com.example.android.popularmovies.data.Reviews(1, reviewsList, 1, reviewsList.size());
        }

        Cursor cursor = context.getContentResolver().query(
                Reviews.REVIEWS,
                null,
                ReviewsColumns.MOVIE_ID + " = ?",
                new String[] { movieId.toString() },
                null);

        try {
            if (cursor == null || !cursor.moveToFirst()) {
                return new com.example.android.popularmovies.data.Reviews(1, reviewsList, 1, reviewsList.size());
            }

            reviewsList.add(getReviewFromCursor(cursor));
            while (cursor.moveToNext()) {
                reviewsList.add(getReviewFromCursor(cursor));
            }

            return new com.example.android.popularmovies.data.Reviews(1, reviewsList, 1, reviewsList.size());
        } finally {
            cursor.close();
        }
    }

    /**
     * Method for getting a review from a cursor
     *
     * @param cursor
     * @return
     */
    private static Review getReviewFromCursor(Cursor cursor) {
        String id = cursor.getString(INDEX_REVIEW_ID);
        String author = cursor.getString(INDEX_REVIEW_AUTHOR);
        String content = cursor.getString(INDEX_REVIEW_CONTENT);
        String iso6391 = cursor.getString(INDEX_REVIEW_ISO_639_1);
        String mediaId = cursor.getString(INDEX_REVIEW_MEDIA_ID);
        String mediaTitle = cursor.getString(INDEX_REVIEW_MEDIA_TITLE);
        String mediaType = cursor.getString(INDEX_REVIEW_MEDIA_TYPE);
        String reviewUrl = cursor.getString(INDEX_REVIEW_URL);

        return new Review(id,
                author,
                content,
                iso6391,
                mediaId,
                mediaTitle,
                mediaType,
                reviewUrl);
    }
}