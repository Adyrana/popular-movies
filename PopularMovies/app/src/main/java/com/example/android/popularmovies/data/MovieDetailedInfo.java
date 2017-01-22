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

package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data class for detailed movie info.
 *
 * uses Lombok, there needs to be an lombok.config file in the project root with these options
 * to make things work properly:
 * <li>
 *     <ul>lombok.addGeneratedAnnotation = false</ul>
 *     <ul>lombok.anyConstructor.suppressConstructorProperties = true</ul>
 * </li>
 *
 * @author Julia Mattjus
 */
@AllArgsConstructor
@Data
public class MovieDetailedInfo implements Parcelable {
    @SerializedName("adult")
    private Boolean adult;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("belongs_to_collection")
    private MovieCollection belongsToCollection;

    @SerializedName("budget")
    private Integer budget;

    @SerializedName("genres")
    private List<Genre> genres;

    @SerializedName("homepage")
    private String homepage;

    @SerializedName("id")
    private Integer id;

    @SerializedName("imdb_id")
    private String imdbId;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("production_companies")
    private List<ProductionCompanies> productionCompanies;

    @SerializedName("production_countries")
    private List<ProductionCountries> productionCountries;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("revenue")
    private Integer revenue;

    @SerializedName("runtime")
    private Integer runtime;

    @SerializedName("spoken_languages")
    private List<SpokenLanguages> spokenLanguages;

    @SerializedName("status")
    private String status;

    @SerializedName("tagline")
    private String tagline;

    @SerializedName("title")
    private String title;

    @SerializedName("video")
    private Boolean video;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("vote_count")
    private Integer voteCount;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.adult);
        dest.writeString(this.backdropPath);
        dest.writeParcelable(this.belongsToCollection, flags);
        dest.writeValue(this.budget);
        dest.writeList(this.genres);
        dest.writeString(this.homepage);
        dest.writeValue(this.id);
        dest.writeString(this.imdbId);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeValue(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeList(this.productionCompanies);
        dest.writeList(this.productionCountries);
        dest.writeString(this.releaseDate);
        dest.writeValue(this.revenue);
        dest.writeValue(this.runtime);
        dest.writeList(this.spokenLanguages);
        dest.writeString(this.status);
        dest.writeString(this.tagline);
        dest.writeString(this.title);
        dest.writeValue(this.video);
        dest.writeValue(this.voteAverage);
        dest.writeValue(this.voteCount);
    }

    protected MovieDetailedInfo(Parcel in) {
        this.adult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.backdropPath = in.readString();
        this.belongsToCollection = in.readParcelable(MovieCollection.class.getClassLoader());
        this.budget = (Integer) in.readValue(Integer.class.getClassLoader());
        this.genres = new ArrayList<Genre>();
        in.readList(this.genres, Genre.class.getClassLoader());
        this.homepage = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.imdbId = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.posterPath = in.readString();
        this.productionCompanies = new ArrayList<ProductionCompanies>();
        in.readList(this.productionCompanies, ProductionCompanies.class.getClassLoader());
        this.productionCountries = new ArrayList<ProductionCountries>();
        in.readList(this.productionCountries, ProductionCountries.class.getClassLoader());
        this.releaseDate = in.readString();
        this.revenue = (Integer) in.readValue(Integer.class.getClassLoader());
        this.runtime = (Integer) in.readValue(Integer.class.getClassLoader());
        this.spokenLanguages = new ArrayList<SpokenLanguages>();
        in.readList(this.spokenLanguages, SpokenLanguages.class.getClassLoader());
        this.status = in.readString();
        this.tagline = in.readString();
        this.title = in.readString();
        this.video = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<MovieDetailedInfo> CREATOR = new Parcelable.Creator<MovieDetailedInfo>() {
        @Override
        public MovieDetailedInfo createFromParcel(Parcel source) {
            return new MovieDetailedInfo(source);
        }

        @Override
        public MovieDetailedInfo[] newArray(int size) {
            return new MovieDetailedInfo[size];
        }
    };
}
