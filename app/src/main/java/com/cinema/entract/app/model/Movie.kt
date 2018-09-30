/*
 * Copyright 2018 Stéphane Baiget
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cinema.entract.app.model

import android.os.Parcel
import android.os.Parcelable
import org.threeten.bp.LocalDate

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class Movie(
    val id: String,
    val title: String,
    val date: LocalDate,
    val schedule: String,
    val isThreeDimension: Boolean,
    val isOriginalVersion: Boolean,
    val coverUrl: String,
    val duration: String,
    val yearOfProduction: String,
    val genre: String,
    val director: String,
    val cast: String,
    val synopsis: String,
    val teaserId: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readSerializable() as LocalDate,
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeSerializable(date)
        parcel.writeString(schedule)
        parcel.writeByte(if (isThreeDimension) 1 else 0)
        parcel.writeByte(if (isOriginalVersion) 1 else 0)
        parcel.writeString(coverUrl)
        parcel.writeString(duration)
        parcel.writeString(yearOfProduction)
        parcel.writeString(genre)
        parcel.writeString(director)
        parcel.writeString(cast)
        parcel.writeString(synopsis)
        parcel.writeString(teaserId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}
