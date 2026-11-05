package com.yuriisurzhykov.pointdetector.konsist

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** The rules are code too: each one must catch what it claims to catch and leave good code alone. */
class RulesSelfTest {

    private val root = "pkg.designsystem"

    private fun file(path: String, text: String) = SourceFile(path, text.trimIndent())

    @Test
    fun `when rule flags only sealed objects without is`() {
        val fixture = file(
            "fixture/State.kt",
            """
            sealed interface State {
                data object Loading : State
                data class Loaded(val n: Int) : State
            }
            enum class Mode { Fast, Slow }
            fun render(s: State, m: Mode) {
                when (s) {
                    State.Loading -> Unit
                    is State.Loaded -> Unit
                }
                when (m) {
                    Mode.Fast -> Unit
                    Mode.Slow -> Unit
                }
            }
            """,
        )
        assertEquals(listOf("fixture/State.kt:8: State.Loading"), ArchitectureRules.whenBranchesWithoutIs(listOf(fixture)))
        assertEquals(listOf("fixture/State.kt"), ArchitectureRules.manyTypesPerFile(listOf(fixture)))
    }

    @Test
    fun `ratchet fails on new and on fixed violations`() {
        assertNull(Ratchet.check("r", found = listOf("a"), known = listOf("a")))
        assertNotNull(Ratchet.check("r", found = listOf("a", "b"), known = listOf("a")))
        assertNotNull(Ratchet.check("r", found = emptyList(), known = listOf("a")))
    }

    @Test
    fun `hardcode rule catches literals and ignores tokens`() {
        val bad = file(
            "x/Bad.kt",
            """
            val a = Modifier.padding(16.dp)
            val b = Color(0xFF112233)
            val c = tween(300)
            val d = graphicsLayer { alpha = 0.5f }
            val e = RoundedCornerShape(8.dp)
            """,
        )
        val good = file(
            "x/Good.kt",
            """
            val a = Modifier.padding(PdTheme.dimensions.spacing.regular)
            val b = Modifier.size(0.dp)
            val c = PdTheme.motion.effects<Float>()
            // padding(16.dp) in a comment is fine
            """,
        )
        // Five bad lines; the last one breaks two patterns at once (a literal dp and a raw shape).
        assertEquals(6, ArchitectureRules.forbiddenCode(listOf(bad), DesignSystemRules.hardcodedTokens).size)
        assertTrue(ArchitectureRules.forbiddenCode(listOf(good), DesignSystemRules.hardcodedTokens).isEmpty())
    }

    @Test
    fun `layering rule forbids importing a higher level`() {
        val atom = file(
            "designsystem/atoms/texts/PdText.kt",
            """
            package pkg.designsystem.atoms.texts
            import pkg.designsystem.molecules.buttons.PdButton
            import pkg.designsystem.theme.PdTheme
            """,
        )
        val molecule = file(
            "designsystem/molecules/buttons/PdButton.kt",
            """
            package pkg.designsystem.molecules.buttons
            import pkg.designsystem.atoms.texts.PdText
            """,
        )
        assertEquals(1, DesignSystemRules.layerViolations(listOf(atom, molecule), root).size)
    }

    @Test
    fun `group rule flags a file directly in a level package`() {
        val loose = file("designsystem/atoms/PdLoose.kt", "package pkg.designsystem.atoms")
        val grouped = file("designsystem/atoms/texts/PdText.kt", "package pkg.designsystem.atoms.texts")
        assertEquals(listOf(loose.path), DesignSystemRules.filesOutsideGroupPackages(listOf(loose, grouped), root))
    }

    @Test
    fun `nullable rule allows primitives and private members only`() {
        val source = file(
            "x/N.kt",
            """
            class Good(
                val title: String?,
                private val hidden: Foo?,
            )
            class Bad(
                val icon: ImageVector?,
                val onClick: (() -> Unit)?,
            )
            """,
        )
        assertEquals(2, DesignSystemRules.nullableStructures(listOf(source)).size)
    }

    @Test
    fun `free composable rule flags public functions and spares internal ones`() {
        val source = file(
            "x/F.kt",
            """
            @Composable
            fun PdFree() = Unit

            @Composable
            internal fun PdInternal() = Unit

            @Composable
            @ReadOnlyComposable
            fun PdAnnotated() = Unit
            """,
        )
        assertEquals(2, DesignSystemRules.freePublicComposables(listOf(source), exemptPathPart = "/theme/").size)
    }

    @Test
    fun `component rule requires a Pd supertype`() {
        val source = file(
            "designsystem/atoms/texts/X.kt",
            """
            package pkg.designsystem.atoms.texts
            class PdGood(private val a: PdTextResolver) : PdBaseComponent(), PdText {
            }
            class Lonely(private val a: Int) {
            }
            enum class Role { A }
            """,
        )
        assertEquals(listOf("designsystem/atoms/texts/X.kt:4: Lonely"), DesignSystemRules.componentsWithoutInterface(listOf(source), root))
    }

    @Test
    fun `signature rules catch base classes and string texts`() {
        val source = file(
            "x/S.kt",
            """
            class Card(
                val button: PdBaseButton,
                val label: String,
                private val hidden: PdBaseButton,
            ) : PdBaseComponent()
            """,
        )
        assertEquals(1, DesignSystemRules.baseTypesInSignatures(listOf(source)).size)
        assertEquals(1, DesignSystemRules.textAsString(listOf(source)).size)
    }
}
