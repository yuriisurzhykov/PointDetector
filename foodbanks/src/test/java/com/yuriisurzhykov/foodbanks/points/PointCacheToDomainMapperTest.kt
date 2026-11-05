package com.yuriisurzhykov.foodbanks.points

import com.yuriisurzhykov.foodbanks.data.favorites.FavoritePointCache
import com.yuriisurzhykov.foodbanks.data.point.WorkingHour
import com.yuriisurzhykov.foodbanks.data.point.cache.HelpFactCache
import com.yuriisurzhykov.foodbanks.data.point.cache.PointCache
import com.yuriisurzhykov.foodbanks.data.point.cache.PointOrigin
import com.yuriisurzhykov.foodbanks.data.point.cache.PointWithHours
import com.yuriisurzhykov.foodbanks.data.point.cache.WorkingHourCache
import com.yuriisurzhykov.foodbanks.domain.points.HelpFact
import com.yuriisurzhykov.foodbanks.domain.points.Point
import com.yuriisurzhykov.foodbanks.domain.points.PointHelp
import com.yuriisurzhykov.foodbanks.domain.points.PointCacheToDomainMapper
import com.yuriisurzhykov.foodbanks.domain.points.WorkingHourCacheToDomainMapper
import com.yuriisurzhykov.geo.LatLng
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PointCacheToDomainMapperTest {

    private val mapper = PointCacheToDomainMapper.Base(WorkingHourCacheToDomainMapper.Base())

    @Test
    fun `test point fields are mapped`() {
        val point = mapper.mapSingle(
            PointWithHours(
                PointCache(7, "city", "Address", "Place", LatLng(1.0, 2.0), PointOrigin.USER),
                listOf(WorkingHourCache("Mon", -1, "09:00", "12:00", 7, 1))
            )
        ).fields()

        assertEquals(7L, point.id)
        assertEquals("Place", point.name)
        assertEquals("Address", point.address)
        assertEquals(LatLng(1.0, 2.0), point.coordinates)
        assertEquals(PointOrigin.USER, point.origin)
        assertEquals(listOf(WorkingHour("Mon", -1, "09:00", "12:00")), point.workingHours)
    }

    @Test
    fun `test point with a favorite is favorite`() {
        val cache = PointCache(7, "city", "Address", "Place")

        val favorite = mapper.mapSingle(
            PointWithHours(cache, emptyList(), listOf(FavoritePointCache(1, 7, "Place", "Address")))
        ).fields()
        val regular = mapper.mapSingle(PointWithHours(cache, emptyList())).fields()

        assertTrue(favorite.favorite)
        assertFalse(regular.favorite)
    }

    @Test
    fun `test help facts are mapped and unknown ones are absent`() {
        val point = mapper.mapSingle(
            PointWithHours(
                PointCache(7, "city", "Address", "Place"),
                emptyList(),
                helpFacts = listOf(
                    HelpFactCache("appointment", true, 7, 1),
                    HelpFactCache("documents", false, 7, 2)
                )
            )
        ).fields()

        assertEquals(
            PointHelp(listOf(HelpFact("appointment", true), HelpFact("documents", false))),
            point.help
        )
    }

    @Test
    fun `test point without help facts has none`() {
        val point = mapper.mapSingle(PointWithHours(PointCache(7, "city", "Address", "Place"), emptyList())).fields()

        assertEquals(PointHelp.None, point.help)
    }

    private data class Fields(
        val id: Long,
        val name: String,
        val address: String,
        val coordinates: LatLng,
        val workingHours: List<WorkingHour>,
        val origin: PointOrigin,
        val favorite: Boolean,
        val help: PointHelp
    )

    private fun Point.fields() = map(object : Point.Mapper<Fields> {
        override fun map(
            id: Long,
            name: String,
            address: String,
            coordinates: LatLng,
            workingHours: List<WorkingHour>,
            origin: PointOrigin,
            favorite: Boolean,
            help: PointHelp
        ) = Fields(id, name, address, coordinates, workingHours, origin, favorite, help)
    })
}
