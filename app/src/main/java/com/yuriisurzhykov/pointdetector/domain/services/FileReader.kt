package com.yuriisurzhykov.pointdetector.domain.services

import android.content.Context
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.jvm.Throws

interface FileReader {
    @Throws(IOException::class, FileNotFoundException::class)
    fun readFile(context: Context, fileName: String): String
}

