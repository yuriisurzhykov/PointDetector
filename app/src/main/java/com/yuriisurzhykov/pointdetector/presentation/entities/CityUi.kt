package com.yuriisurzhykov.pointdetector.presentation.entities

import com.yuriisurzhykov.foodbanks.domain.city.City

data class CityUi(
    val code: String,
    val name: String,
    val region: String
) {

    val label: String
        get() = if (region.isBlank()) name else "$name, $region"

    object Factory : City.Mapper<CityUi> {

        override fun map(cityName: String, cityCode: String, country: String, region: String) =
            CityUi(cityCode, cityName, region)
    }
}
