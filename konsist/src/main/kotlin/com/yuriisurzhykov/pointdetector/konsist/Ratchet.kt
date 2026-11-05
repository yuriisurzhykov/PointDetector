package com.yuriisurzhykov.pointdetector.konsist

/**
 * Compares the violations a rule found with the violations that are known and tolerated.
 *
 * Fails on a **new** violation, and also on a known one that is gone, so the allowlist can
 * only shrink: whoever fixes a file must also delete it from the list.
 */
object Ratchet {

    fun check(rule: String, found: Collection<String>, known: Collection<String>): String? {
        val new = found.toSortedSet() - known.toSet()
        val fixed = known.toSortedSet() - found.toSet()
        if (new.isEmpty() && fixed.isEmpty()) return null
        return buildString {
            appendLine("Rule '$rule' failed.")
            if (new.isNotEmpty()) {
                appendLine("New violations (fix them; do not add them to the allowlist):")
                new.forEach { appendLine("  - $it") }
            }
            if (fixed.isNotEmpty()) {
                appendLine("Fixed, now remove them from the allowlist:")
                fixed.forEach { appendLine("  - $it") }
            }
        }
    }
}
