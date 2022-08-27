package com.yuriisurzhykov.pointdetector.domain.services

import com.yuriisurzhykov.pointdetector.domain.entities.Point

sealed class PointsSortingTypeService(val sortingType: String) {

    abstract fun sort(points: List<Point>): List<Point>

    object SortByDistance : PointsSortingTypeService("distance") {
        override fun sort(points: List<Point>) = points.sortedBy { it.distance }
    }

    object SortByCreatedDate : PointsSortingTypeService("creation_date") {
        override fun sort(points: List<Point>) = points
    }

}