package com.example.android.popularmovies;

/**
 * The interface that receives onClick messages.
 *
 * @author Julia Mattjus
 */
public interface IMovieAdapterOnClickHandler {

    /**
     * On click method intended for movies so that an the detailed view of a movie can be shown.
     *
     * @param movieId
     */
    void onClick(Integer movieId);
}
