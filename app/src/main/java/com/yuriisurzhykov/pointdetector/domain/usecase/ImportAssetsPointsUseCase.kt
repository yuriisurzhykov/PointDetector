package com.yuriisurzhykov.pointdetector.domain.usecase

import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.domain.services.JsonAssetListFileReader
import java.util.ArrayList
import javax.inject.Inject

interface ImportAssetsPointsUseCase {

    fun importPointsList(fileName: String): List<Point>

    class Base @Inject constructor(
        private val jsonAssetListFileReader: JsonAssetListFileReader
    ) : ImportAssetsPointsUseCase {
        @Suppress("UNCHECKED_CAST")
        override fun importPointsList(fileName: String): List<Point> {
            return jsonAssetListFileReader.parse(fileName, ArrayList::class.java) as ArrayList<Point>
        }
    }
}