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
 * Data class for production companies.
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
public class ProductionCompanies implements Parcelable {
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
    }

    protected ProductionCompanies(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ProductionCompanies> CREATOR = new Parcelable.Creator<ProductionCompanies>() {
        @Override
        public ProductionCompanies createFromParcel(Parcel source) {
            return new ProductionCompanies(source);
        }

        @Override
        public ProductionCompanies[] newArray(int size) {
            return new ProductionCompanies[size];
        }
    };
}
