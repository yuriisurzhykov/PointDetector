package com.yuriisurzhykov.pointdetector.konsist

/**
 * Finds `when` branches naming one of [sealedObjects] (`Parent.Object`) without `is`.
 * Only lines directly inside a `when (...) { }` block are inspected, so arguments and
 * enum declarations that look alike are ignored.
 */
internal class WhenBranches(private val sealedObjects: Set<String>) {

    private val condition = Regex(
        """^\s*((?:(?:is\s+)?(?:[A-Z]\w*\.)*[A-Z]\w*\s*,\s*)*(?:is\s+)?(?:[A-Z]\w*\.)*[A-Z]\w*)\s*(?:,|->)""",
    )
    private val whenOpen = Regex("""\bwhen\b[^{]*$""")
    private val whenWord = Regex("""\bwhen\b""")
    private val stringLiteral = Regex(""""(?:\\.|[^"\\])*"""")

    fun violations(files: List<SourceFile>): List<String> = files.flatMap(::violationsIn)

    private fun violationsIn(file: SourceFile): List<String> {
        val found = mutableListOf<String>()
        var depth = 0
        val whenDepths = ArrayDeque<Int>()
        var pendingWhen = false
        file.lines.forEachIndexed { index, line ->
            val code = stringLiteral.replace(line.substringBefore("//"), "\"\"")
            val atBranchLevel = whenDepths.isNotEmpty() && depth == whenDepths.last()
            code.forEachIndexed { i, ch ->
                if (ch == '{') {
                    depth++
                    if (pendingWhen || whenOpen.containsMatchIn(code.substring(0, i))) {
                        whenDepths.addLast(depth)
                        pendingWhen = false
                    }
                } else if (ch == '}') {
                    if (whenDepths.isNotEmpty() && depth == whenDepths.last()) whenDepths.removeLast()
                    depth--
                }
            }
            if (whenWord.containsMatchIn(code) && '{' !in code.substringAfter("when")) pendingWhen = true
            if (atBranchLevel) found += offendingTokens(code).map { "${file.path}:${index + 1}: $it" }
        }
        return found
    }

    private fun offendingTokens(line: String): List<String> {
        val match = condition.find(line) ?: return emptyList()
        return match.groupValues[1].split(',').map { it.trim() }
            .filter { !it.startsWith("is ") && it in sealedObjects }
    }
}
