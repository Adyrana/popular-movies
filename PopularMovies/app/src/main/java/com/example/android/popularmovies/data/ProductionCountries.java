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

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data class for production countries.
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
public class ProductionCountries implements Parcelable {
    @SerializedName("iso_3166_1")
    private String iso31661;

    @SerializedName("name")
    private String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.iso31661);
        dest.writeString(this.name);
    }

    protected ProductionCountries(Parcel in) {
        this.iso31661 = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ProductionCountries> CREATOR = new Parcelable.Creator<ProductionCountries>() {
        @Override
        public ProductionCountries createFromParcel(Parcel source) {
            return new ProductionCountries(source);
        }

        @Override
        public ProductionCountries[] newArray(int size) {
            return new ProductionCountries[size];
        }
    };
}
