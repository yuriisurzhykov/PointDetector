package com.yuriisurzhykov.pointdetector.konsist

import kotlin.test.Test
import kotlin.test.fail

/** Structural rules of the new Compose modules. Limits are deliberate: change them in review only. */
class ArchitectureTest {

    private val composeModules get() = ProjectSources.designSystem + ProjectSources.toast

    @Test
    fun `every type lives in its own file`() = ratchet(
        "one top-level type per file",
        ArchitectureRules.manyTypesPerFile(composeModules),
        KnownViolations.manyTypesPerFile,
    )

    @Test
    fun `production files stay short`() = ratchet(
        "production file <= $MAX_FILE_LINES lines",
        ArchitectureRules.longFiles(composeModules, MAX_FILE_LINES),
        KnownViolations.longFiles,
    )

    @Test
    fun `when branches on sealed objects use is`() = ratchet(
        "`is` for sealed objects in when",
        ArchitectureRules.whenBranchesWithoutIs(composeModules),
        known = emptyList(),
    )

    @Test
    fun `the design system is a leaf`() = ratchet(
        "design system depends on nothing of the app",
        ArchitectureRules.forbiddenImports(
            ProjectSources.designSystem,
            listOf("${ProjectSources.APP_ROOT}.").map { it },
        ).filterNot { it.contains(": ${ProjectSources.DESIGN_SYSTEM_ROOT}.") },
        known = emptyList(),
    )

    @Test
    fun `the toast depends only on the design system`() = ratchet(
        "toast imports its own package and the design system only",
        ArchitectureRules.forbiddenImports(ProjectSources.toast, listOf("${ProjectSources.APP_ROOT}."))
            .filterNot { it.contains(": ${ProjectSources.DESIGN_SYSTEM_ROOT}.") || it.contains(": ${ProjectSources.TOAST_ROOT}.") },
        known = emptyList(),
    )

    private fun ratchet(rule: String, found: List<String>, known: List<String>) {
        Ratchet.check(rule, found, known)?.let { fail(it) }
    }

    private companion object {
        const val MAX_FILE_LINES = 250
    }
}
