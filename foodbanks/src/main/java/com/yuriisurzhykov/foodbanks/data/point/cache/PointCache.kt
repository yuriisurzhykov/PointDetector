package com.yuriisurzhykov.foodbanks.data.point.cache

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.yuriisurzhykov.foodbanks.data.city.cache.CityCache
import com.yuriisurzhykov.foodbanks.data.point.LatLng
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour

@Entity(
    tableName = "PointCache",
    foreignKeys = [ForeignKey(
        entity = CityCache::class,
        parentColumns = ["nameCode"],
        childColumns = ["cityCodeId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PointCache(
    @PrimaryKey(autoGenerate = true)
    val pointId: Long,
    val cityCodeId: String,
    val address: String,
    val placeName: String,
    @Embedded
    val coordinates: LatLng,
    @Embedded
    val workingHours: List<WorkingHour>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PointCache

        if (cityCodeId != other.cityCodeId) return false
        if (address != other.address) return false
        if (placeName != other.placeName) return false
        if (coordinates != other.coordinates) return false
        if (workingHours != other.workingHours) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cityCodeId.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + placeName.hashCode()
        result = 31 * result + coordinates.hashCode()
        result = 31 * result + workingHours.hashCode()
        return result
    }
}