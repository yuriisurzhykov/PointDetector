package com.yuriisurzhykov.pointsdetector.uicomponents.workday.entity

import android.os.Parcel
import android.os.Parcelable
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolderItem
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class WeekDay(
    var dayName: String = "",
    var dayValue: Int = 0,
    var hoursFrom: String = "",
    var hoursTo: String = ""
) : Parcelable, java.io.Serializable, ViewHolderItem {

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

    override fun areContentsTheSame(other: Any): Boolean {
        return other is WeekDay && other.dayName == dayName
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return other is WeekDay && other.dayName == dayName
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
