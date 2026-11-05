package com.yuriisurzhykov.pointdetector.konsist

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

/** The contract of the design system, enforced. Every rule here must hold with an empty allowlist. */
class DesignSystemTest {

    private val root = ProjectSources.COMPONENTS_ROOT

    @Test
    fun `atoms, molecules and organisms only depend downwards`() = holds(
        "atomic layering: particles < atoms < molecules < organisms",
        DesignSystemRules.layerViolations(ProjectSources.components, root),
    )

    @Test
    fun `atomic levels are split into group packages`() = holds(
        "atoms/buttons, molecules/status, organisms/surfaces: never a file directly in the level",
        DesignSystemRules.filesOutsideGroupPackages(ProjectSources.components, root),
    )

    @Test
    fun `dimensions, colours, shapes and motion come only from PdTheme`() = holds(
        "no hardcoded dp / sp / colour / alpha / duration / shape / font outside token files",
        ArchitectureRules.forbiddenCode(ProjectSources.tokenConsumers, DesignSystemRules.hardcodedTokens),
    )

    @Test
    fun `resources are read only by resolvers`() = holds(
        "stringResource and painterResource live in the resolver package",
        ArchitectureRules.forbiddenCode(
            (ProjectSources.designSystem + ProjectSources.toast).filterNot { it.path.contains("/resolver/") },
            DesignSystemRules.resourceAccess,
        ),
    )

    @Test
    fun `user-facing texts are not string literals`() = holds(
        "no Cyrillic text in string literals",
        ArchitectureRules.forbiddenCode(ProjectSources.designSystem + ProjectSources.toast, listOf(DesignSystemRules.cyrillicLiteral)),
    )

    @Test
    fun `no time or business types in UI`() {
        holds(
            "design system: no java.time, kotlin.time, kotlinx.datetime, Date",
            ArchitectureRules.forbiddenImports(ProjectSources.designSystem, DesignSystemRules.businessTimeImports),
        )
        holds(
            "toast: no java.time, kotlinx.datetime (Duration of a timer is a UI value)",
            ArchitectureRules.forbiddenImports(ProjectSources.toast, listOf("java.time.", "kotlinx.datetime.", "java.util.Date")),
        )
    }

    @Test
    fun `no nullable structures in the public API`() = holds(
        "nullable primitives are allowed; nullable structures and lambdas are not",
        DesignSystemRules.nullableStructures(ProjectSources.designSystem),
    )

    @Test
    fun `no public composable function outside the theme`() = holds(
        "every component is an interface plus classes, not a free composable",
        DesignSystemRules.freePublicComposables(ProjectSources.designSystem, exemptPathPart = "/designsystem/theme/"),
    )

    @Test
    fun `every component class implements a Pd interface`() = holds(
        "a class in atoms / molecules / organisms extends PdBase* or implements Pd*",
        DesignSystemRules.componentsWithoutInterface(ProjectSources.components, root),
    )

    @Test
    fun `base classes never appear in a public signature`() = holds(
        "consumers take the interface, never the PdBase* scaffolding",
        DesignSystemRules.baseTypesInSignatures(ProjectSources.designSystem + ProjectSources.toast),
    )

    @Test
    fun `visible text is a resolver, not a String`() = holds(
        "text / label / title / message parameters are PdTextResolver",
        DesignSystemRules.textAsString(ProjectSources.designSystem),
    )

    @Test
    fun `app compose code styles itself only through the design system`() = holds(
        "no raw styling in app code",
        ArchitectureRules.forbiddenImports(ProjectSources.appCompose, RAW_STYLING_IMPORTS) +
            ArchitectureRules.forbiddenCode(ProjectSources.appCompose, DesignSystemRules.hardcodedTokens),
    )

    @Test
    fun `the design system report`() {
        val metrics = DesignSystemRules.metrics(ProjectSources.designSystem, ProjectSources.tokenConsumers)
        println("DESIGN SYSTEM METRICS: $metrics")
        assertTrue(metrics.getValue("interfaces") > 0, "no interfaces found: are the sources scanned?")
    }

    private fun holds(rule: String, violations: List<String>) {
        if (violations.isNotEmpty()) {
            fail(buildString {
                appendLine("Rule '$rule' failed:")
                violations.sorted().forEach { appendLine("  - $it") }
            })
        }
    }

    private companion object {
        val RAW_STYLING_IMPORTS = listOf(
            "androidx.compose.material.",
            "androidx.compose.material3.",
            "androidx.compose.foundation.shape.",
            "androidx.compose.ui.text.font.",
        )
    }
}
