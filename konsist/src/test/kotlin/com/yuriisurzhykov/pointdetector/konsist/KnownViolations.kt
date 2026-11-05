package com.yuriisurzhykov.pointdetector.konsist

/**
 * Violations that existed when a rule was introduced. They are tolerated, not accepted:
 * the lists may only shrink (see [Ratchet]). Fix a file, then delete its line here.
 *
 * The design system is new, so every list starts empty and must stay empty.
 */
object KnownViolations {

    val manyTypesPerFile: List<String> = emptyList()

    val longFiles: List<String> = emptyList()
}
