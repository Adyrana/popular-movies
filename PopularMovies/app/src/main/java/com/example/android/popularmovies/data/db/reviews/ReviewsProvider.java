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

    @TableEndpoint(table = ReviewsDatabase.REVIEWS) public static class Reviews {

        @ContentUri(
                path = "reviews",
                type = "vnd.android.cursor.dir/reviews",
                defaultSort = ReviewsColumns._ID
        )
        public static final Uri REVIEWS = Uri.parse("content://" + AUTHORITY + "/reviews");
    }

    public static final int INDEX_REVIEW_ID = 0;
    public static final int INDEX_REVIEW_AUTHOR = 1;
    public static final int INDEX_REVIEW_CONTENT = 2;
    public static final int INDEX_REVIEW_ISO_639_1 = 3;
    public static final int INDEX_REVIEW_MEDIA_ID = 4;
    public static final int INDEX_REVIEW_MEDIA_TITLE = 5;
    public static final int INDEX_REVIEW_MEDIA_TYPE = 6;
    public static final int INDEX_REVIEW_URL = 7;

    private static final String TAG = ReviewsProvider.class.getSimpleName();

    public static void write(Context context, com.example.android.popularmovies.data.Reviews reviews) {

        List<ContentValues> contentValuesList = new ArrayList<ContentValues>();

        for(Review review : reviews.getResults()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ReviewsColumns._ID, review.getId());
            contentValues.put(ReviewsColumns.AUTHOR, review.getAuthor());
            contentValues.put(ReviewsColumns.CONTENT, review.getContent());
            contentValues.put(ReviewsColumns.ISO_639_1, review.getIso6391());
            contentValues.put(ReviewsColumns.MEDIA_ID, review.getMediaId());
            contentValues.put(ReviewsColumns.MEDIA_TITLE, review.getMediaTitle());
            contentValues.put(ReviewsColumns.MEDIA_TYPE, review.getMediaType());
            contentValues.put(ReviewsColumns.URL, review.getUrl());
        }

        context.getContentResolver().bulkInsert(Reviews.REVIEWS, contentValuesList.toArray(new ContentValues[contentValuesList.size()]));
    }

    public static void remove(Context context, List<String> reviewIds) {

        Log.d(TAG, "remove - reviewIds: " + JsonUtility.toJson(reviewIds.toArray(new String[reviewIds.size()])));

        if(reviewIds == null || reviewIds.size() == 0) {
            return;
        }

        context.getContentResolver().delete(
                Reviews.REVIEWS,
                buildSelection(reviewIds),
                reviewIds.toArray(new String[reviewIds.size()]));
    }

    public static com.example.android.popularmovies.data.Reviews getReviewsFromFromIds(Context context, List<String> reviewIds) {

        Log.d(TAG, "getReviewsFromFromIds - reviewIds: " + JsonUtility.toJson(reviewIds));
        List<Review> reviewsList = new ArrayList<>();

        if(reviewIds == null || reviewIds.size() == 0) {
            return new com.example.android.popularmovies.data.Reviews(1, reviewsList, 1, reviewsList.size());
        }

        Cursor cursor = context.getContentResolver().query(
                Reviews.REVIEWS,
                null,
                buildSelection(reviewIds),
                reviewIds.toArray(new String[reviewIds.size()]),
                null);

        if(cursor == null || !cursor.moveToFirst()) {
            return new com.example.android.popularmovies.data.Reviews(1, reviewsList, 1, reviewsList.size());
        }

        reviewsList.add(getReviewFromCursor(cursor));
        while(cursor.moveToNext()) {
            reviewsList.add(getReviewFromCursor(cursor));
        }

        return new com.example.android.popularmovies.data.Reviews(1, reviewsList, 1, reviewsList.size());
    }

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

    private static String buildSelection(List<String> ids) {
        String selection = VideosColumns._ID + " in (";
        for(int i = 0; i < ids.size(); i++) {
            selection += "?, ";
        }
        return selection.substring(0, selection.length() - 2) + ")";
    }
}