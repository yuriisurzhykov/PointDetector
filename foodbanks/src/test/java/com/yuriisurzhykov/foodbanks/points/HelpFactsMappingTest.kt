package com.yuriisurzhykov.foodbanks.points

import com.yuriisurzhykov.foodbanks.data.point.CloudToCachePointMapper
import com.yuriisurzhykov.foodbanks.data.point.cache.HelpFactCache
import com.yuriisurzhykov.foodbanks.data.point.cloud.PointCloud
import com.yuriisurzhykov.foodbanks.data.point.user.UserPoint
import com.yuriisurzhykov.foodbanks.data.point.user.UserPointToCacheMapper
import com.yuriisurzhykov.geo.LatLng
import org.junit.Assert.assertEquals
import org.junit.Test

class HelpFactsMappingTest {

    private val cloudMapper = CloudToCachePointMapper.Base()
    private val userMapper = UserPointToCacheMapper.Base()

    @Test
    fun `test help of a cloud point becomes cache facts`() {
        val cache = cloudMapper.map(
            PointCloud(
                address = "Address",
                coordinates = LatLng(1.0, 2.0),
                placeName = "Place",
                cityCode = "city",
                workingHours = emptyList(),
                help = linkedMapOf("appointment" to true, "documents" to false)
            )
        )

        assertEquals(
            listOf(HelpFactCache("appointment", true, 0), HelpFactCache("documents", false, 0)),
            cache.helpFacts
        )
    }

    @Test
    fun `test cloud point without help has no facts`() {
        val cache = cloudMapper.map(PointCloud("Address", LatLng(1.0, 2.0), "Place", "city", emptyList()))

        assertEquals(emptyList<HelpFactCache>(), cache.helpFacts)
    }

    @Test
    fun `test only the answered facts of a user point are stored`() {
        val cache = userMapper.map(
            UserPoint(
                cityCode = "city",
                placeName = "Place",
                address = "Address",
                coordinates = LatLng(1.0, 2.0),
                workingHours = emptyList(),
                help = mapOf("appointment" to true)
            )
        )

        assertEquals(listOf(HelpFactCache("appointment", true, 0)), cache.helpFacts)
    }
}
