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

package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data class for a video object.
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
public class Video implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("iso_639_1")
    private String iso6391;

    @SerializedName("iso_3166_1")
    private String iso31661;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    @SerializedName("size")
    private Integer size;

    @SerializedName("type")
    private String type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.iso6391);
        dest.writeString(this.iso31661);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeValue(this.size);
        dest.writeString(this.type);
    }

    protected Video(Parcel in) {
        this.id = in.readString();
        this.iso6391 = in.readString();
        this.iso31661 = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = (Integer) in.readValue(Integer.class.getClassLoader());
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
