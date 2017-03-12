package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data class for a review.
 *
 * Uses Lombok, there needs to be an lombok.config file in the project root with these options
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
public class Review implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @SerializedName("iso_639_1")
    private String iso6391;

    @SerializedName("media_id")
    private String mediaId;

    @SerializedName("media_title")
    private String mediaTitle;

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("url")
    private String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.iso6391);
        dest.writeString(this.mediaId);
        dest.writeString(this.mediaTitle);
        dest.writeString(this.mediaType);
        dest.writeString(this.url);
    }

    protected Review(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.iso6391 = in.readString();
        this.mediaId = in.readString();
        this.mediaTitle = in.readString();
        this.mediaType = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
