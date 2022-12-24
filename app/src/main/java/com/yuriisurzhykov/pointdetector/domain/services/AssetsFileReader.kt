package com.yuriisurzhykov.pointdetector.domain.services

import android.content.Context
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import kotlin.jvm.Throws

open class AssetsFileReader @Inject constructor() : FileReader {

    protected open fun loadFile(context: Context, fileName: String): InputStream {
        return context.assets.open(fileName)
    }

    @Throws(IOException::class, FileNotFoundException::class)
    override fun readFile(context: Context, fileName: String): String {
        return loadFile(context, fileName).bufferedReader().use { it.readText() }
    }
}