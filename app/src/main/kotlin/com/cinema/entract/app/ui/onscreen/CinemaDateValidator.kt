package com.cinema.entract.app.ui.onscreen

import android.os.Parcel
import android.os.Parcelable
import com.google.android.material.datepicker.CalendarConstraints

class CinemaDateValidator(private val minimumDate: Long, private val maximumDate: Long) :
    CalendarConstraints.DateValidator {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong()
    )

    override fun isValid(date: Long): Boolean = date in minimumDate..maximumDate

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(minimumDate)
        parcel.writeLong(maximumDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CinemaDateValidator> {
        override fun createFromParcel(parcel: Parcel): CinemaDateValidator {
            return CinemaDateValidator(parcel)
        }

        override fun newArray(size: Int): Array<CinemaDateValidator?> {
            return arrayOfNulls(size)
        }
    }
}
