package com.yuriisurzhykov.pointdetector.domain.services

import com.yuriisurzhykov.pointdetector.presentation.entities.PointUi

sealed class PointsSortingTypeService(val sortingType: String) {

    abstract fun sort(points: List<PointUi>): List<PointUi>

    object SortByDistance : PointsSortingTypeService("distance") {
        override fun sort(points: List<PointUi>) = points.sortedBy { it.distanceDouble }
    }

    object SortByCreatedDate : PointsSortingTypeService("creation_date") {
        override fun sort(points: List<PointUi>) = points
    }

}