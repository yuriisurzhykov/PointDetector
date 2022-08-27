package com.yuriisurzhykov.pointsdetector.uicomponents

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

@Serializable
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

    fun isCorrect() = hoursFrom.isNotEmpty() && hoursTo.isNotEmpty() && hoursFrom.before(hoursTo)

    companion object CREATOR : Parcelable.Creator<WeekDay> {
        override fun createFromParcel(parcel: Parcel): WeekDay {
            return WeekDay(parcel)
        }

        override fun newArray(size: Int): Array<WeekDay?> {
            return arrayOfNulls(size)
        }
    }

}

private fun String.before(hoursTo: String): Boolean {
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return try {
        timeFormatter.parse(this)?.before(timeFormatter.parse(hoursTo)) ?: false
    } catch (e: Exception) {
        false
    }
}
