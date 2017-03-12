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
