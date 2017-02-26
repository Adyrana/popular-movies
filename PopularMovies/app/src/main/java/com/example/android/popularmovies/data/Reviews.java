package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Julia Mattjus
 */
@AllArgsConstructor
@Data
public class Reviews implements Parcelable {

    @SerializedName("page")
    private Integer page;

    @SerializedName("results")
    private List<Review> results;

    @SerializedName("total_pages")
    private Integer totalPages;

    @SerializedName("total_results")
    private Integer totalResults;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.page);
        dest.writeList(this.results);
        dest.writeValue(this.totalPages);
        dest.writeValue(this.totalResults);
    }

    protected Reviews(Parcel in) {
        this.page = (Integer) in.readValue(Integer.class.getClassLoader());
        this.results = new ArrayList<Review>();
        in.readList(this.results, Review.class.getClassLoader());
        this.totalPages = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalResults = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Reviews> CREATOR = new Parcelable.Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel source) {
            return new Reviews(source);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };
}
