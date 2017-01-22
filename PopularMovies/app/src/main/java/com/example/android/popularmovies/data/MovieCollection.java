/*
 * MIT License
 *
 * Copyright (c) 2009-2016 The Project Lombok Authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data class defining a movie collection.
 *
 * uses Lombok, there needs to be an lombok.config file in the project root with these options
 * to make things work properly:
 * <ul>
 *     <li>lombok.addGeneratedAnnotation = false</li>
 *     <li>lombok.anyConstructor.suppressConstructorProperties = true</li>
 * </ul>
 *
 * @author Julia Mattjus
 */
@AllArgsConstructor
@Data
public class MovieCollection implements Parcelable {
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
    }

    protected MovieCollection(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
    }

    public static final Parcelable.Creator<MovieCollection> CREATOR = new Parcelable.Creator<MovieCollection>() {
        @Override
        public MovieCollection createFromParcel(Parcel source) {
            return new MovieCollection(source);
        }

        @Override
        public MovieCollection[] newArray(int size) {
            return new MovieCollection[size];
        }
    };
}
