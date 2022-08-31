package com.yuriisurzhykov.pointdetector.domain.services

import android.content.Context
import com.google.gson.Gson
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import java.util.ArrayList
import javax.inject.Inject
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class JsonAssetListFileReader @Inject constructor(
    private val assetsFileReader: AssetsFileReader,
    private val context: Context,
    private val jsonReader: Gson
) : JsonFileParser<ArrayList<*>> {

    @OptIn(ExperimentalStdlibApi::class)
    override fun parse(fileName: String, clazz: Class<out ArrayList<*>>): ArrayList<*> {
        val stringFile = assetsFileReader.readFile(context, fileName)
        return jsonReader.fromJson(stringFile, typeOf<List<Point>>().javaType)
    }
}