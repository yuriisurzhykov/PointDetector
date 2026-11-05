package com.yuriisurzhykov.pointdetector.konsist

/**
 * A Kotlin source file as the rules see it: a path relative to the repository root
 * (`designsystem/src/main/...`) and its text. The rules are plain functions over these,
 * so they can be tested on string fixtures without any project on disk.
 */
data class SourceFile(
    val path: String,
    val text: String,
) {
    val lines: List<String> by lazy { text.lines() }

    val imports: List<String> by lazy {
        lines.filter { it.startsWith("import ") }.map { it.removePrefix("import ").substringBefore(" as ").trim() }
    }

    val packageName: String by lazy {
        lines.firstOrNull { it.startsWith("package ") }?.removePrefix("package ")?.trim().orEmpty()
    }

    val fileName: String get() = path.substringAfterLast('/')
}
