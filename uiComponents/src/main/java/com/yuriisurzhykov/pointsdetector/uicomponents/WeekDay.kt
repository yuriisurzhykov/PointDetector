package com.yuriisurzhykov.pointsdetector.uicomponents

import android.os.Parcel
import android.os.Parcelable

data class WeekDay(
    val dayName: String,
    val dayValue: Int,
    val hoursFrom: String,
    val hoursTo: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dayName)
        parcel.writeInt(dayValue)
        parcel.writeString(hoursFrom)
        parcel.writeString(hoursTo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeekDay> {
        override fun createFromParcel(parcel: Parcel): WeekDay {
            return WeekDay(parcel)
        }

        override fun newArray(size: Int): Array<WeekDay?> {
            return arrayOfNulls(size)
        }
    }

}