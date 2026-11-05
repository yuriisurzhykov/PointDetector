package com.yuriisurzhykov.pointdetector.konsist

/**
 * Rules of the Compose design system as pure functions over [SourceFile]s. Every rule returns
 * stable violation strings for [Ratchet.check]; an empty list means the rule holds.
 */
object DesignSystemRules {

    /** Literal metrics, colours, shapes, fonts and timings. `0.dp` is not a token and stays allowed. */
    val hardcodedTokens: List<Regex> = listOf(
        Regex("""(?<![\w.])[1-9]\d*(?:\.\d+)?f?\.(?:dp|sp)\b"""),
        Regex("""Color\(0x"""),
        Regex("""\b(?:alpha|initialScale|targetScale)\s*=\s*\d"""),
        Regex("""\btween\(\s*\d"""),
        Regex("""\bdurationMillis\s*=\s*\d"""),
        Regex("""\bdelayMillis\s*=\s*\d"""),
        Regex("""\bfillMax(?:Width|Height|Size)\(\s*0?\.\d"""),
        Regex("""\bRoundedCornerShape\("""),
        Regex("""(?<![\w.])TextStyle\("""),
        Regex("""\bFontWeight\."""),
    )

    /** Resource access that only a resolver may perform. */
    val resourceAccess: List<Regex> = listOf(
        Regex("""\b(?:stringResource|pluralStringResource|painterResource|vectorResource)\("""),
        Regex("""@(?:StringRes|PluralsRes|DrawableRes)\b"""),
    )

    /** Types of business logic that must not appear in UI models or composables. */
    val businessTimeImports: List<String> = listOf(
        "java.time.",
        "kotlin.time.",
        "kotlinx.datetime.",
        "java.util.Date",
        "java.util.Calendar",
    )

    val cyrillicLiteral: Regex = Regex(""""[^"\n]*\p{IsCyrillic}""")

    private val particleLayers = setOf("theme", "resolver", "icons", "component", "model")
    private val levels = mapOf("atoms" to 1, "molecules" to 2, "organisms" to 3)

    private val nullableType = Regex("""\b([A-Z]\w*)\?""")
    private val nullableLambda = Regex("""\)\?\s*[=,)]""")
    private val allowedNullable = setOf("String", "Int", "Boolean", "Float", "Long", "Double")
    private val baseSupertype = Regex("""(?:\)|\bobject\s+\w+)\s*:\s*PdBase\w+\(""")
    private val baseInSignature =Regex("""[\w>]\s*:\s*PdBase\w+\s*(?:[,)=]|$)""")
    private val textAsString = Regex("""\b(?:text|label|title|message|description|hint|subtitle|caption):\s*String\b""")
    private val classDeclaration = Regex(
        """^(?:@\w+(?:\([^)]*\))?\s+)*(?:(?:public|abstract|open|data|final)\s+)*(?:class|object)\s+(\w+)""",
    )

    /**
     * A file may import only layers at its own level or below: particles (theme, resolver, icons,
     * component, model) < atoms < molecules < organisms.
     */
    fun layerViolations(files: List<SourceFile>, root: String): List<String> =
        files.flatMap { file ->
            val own = levelOf(file.packageName, root) ?: return@flatMap emptyList()
            file.imports.filter { import -> (levelOf(import, root) ?: 0) > own }.map { "${file.path}: $it" }
        }

    /** Atoms, molecules and organisms must live in a group package (`atoms/buttons`), never in the level itself. */
    fun filesOutsideGroupPackages(files: List<SourceFile>, root: String): List<String> =
        files.filter { file ->
            val relative = file.packageName.removePrefix("$root.")
            file.packageName.startsWith("$root.") && relative in levels.keys
        }.map { it.path }

    /** Public composable functions outside the theme: every component must be an interface plus classes. */
    fun freePublicComposables(files: List<SourceFile>, exemptPathPart: String): List<String> =
        files.filterNot { it.path.contains(exemptPathPart) }.flatMap { file ->
            file.lines.withIndex().mapNotNull { (index, line) ->
                if (!line.startsWith("@Composable")) return@mapNotNull null
                var next = index + 1
                while (next < file.lines.size && file.lines[next].startsWith("@")) next++
                val declaration = file.lines.getOrNull(next).orEmpty()
                val isPublicFunction = declaration.startsWith("fun ") || declaration.startsWith("public fun ")
                if (isPublicFunction) "${file.path}:${next + 1}: ${declaration.trim()}" else null
            }
        }

    /** Nullable structured types or nullable lambdas in a non-private declaration. Nullable primitives are fine. */
    fun nullableStructures(files: List<SourceFile>): List<String> =
        publicLines(files).flatMap { (file, index, line) ->
            if ("fun equals(" in line) return@flatMap emptyList()
            val types = nullableType.findAll(line).map { it.groupValues[1] }.filter { it !in allowedNullable }.toList()
            val lambdas = if (nullableLambda.containsMatchIn(line)) listOf("lambda") else emptyList()
            (types + lambdas).map { "${file.path}:${index + 1}: nullable $it" }
        }

    /** `PdBase*` abstract classes are scaffolding: no public signature may take or return one. */
    fun baseTypesInSignatures(files: List<SourceFile>): List<String> =
        publicLines(files).filter { (_, _, line) -> baseInSignature.containsMatchIn(line) }
            .map { (file, index, line) -> "${file.path}:${index + 1}: ${line.trim()}" }

    /** Visible text must be a resolver, not a `String`. */
    fun textAsString(files: List<SourceFile>): List<String> =
        publicLines(files).filter { (_, _, line) -> textAsString.containsMatchIn(line) }
            .map { (file, index, line) -> "${file.path}:${index + 1}: ${line.trim()}" }

    /**
     * Every public class or object in atoms / molecules / organisms must extend a `PdBase*`
     * class or implement a `Pd*` interface. Enums and interfaces are not classes here.
     */
    fun componentsWithoutInterface(files: List<SourceFile>, root: String): List<String> =
        files.filter { levelOf(it.packageName, root) in levels.values }.flatMap { file ->
            file.lines.withIndex().mapNotNull { (index, line) ->
                val match = classDeclaration.find(line) ?: return@mapNotNull null
                if (line.startsWith(" ") || line.contains("enum class") || line.startsWith("internal") || line.startsWith("private")) {
                    return@mapNotNull null
                }
                val header = file.lines.drop(index).joinToString("\n").substringBefore("{").take(HEADER_LIMIT)
                val supertypes = stripParentheses(header).substringAfter(":", "")
                if (Regex("""\bPd\w+""").containsMatchIn(supertypes)) null else "${file.path}:${index + 1}: ${match.groupValues[1]}"
            }
        }

    /** Counts that describe the design system; a falling `violations` and a rising `implementations` are the goal. */
    fun metrics(files: List<SourceFile>, tokenConsumers: List<SourceFile> = files): Map<String, Int> {
        val interfaces = files.sumOf { f -> f.lines.count { it.startsWith("interface ") || it.startsWith("sealed interface ") } }
        val abstractBases = files.sumOf { f -> f.lines.count { it.startsWith("abstract class ") } }
        val implementations = files.sumOf { f -> baseSupertype.findAll(f.text).count() }
        return linkedMapOf(
            "files" to files.size,
            "lines" to files.sumOf { it.lines.size },
            "interfaces" to interfaces,
            "abstractBases" to abstractBases,
            "implementations" to implementations,
            "hardcodedTokens" to ArchitectureRules.forbiddenCode(tokenConsumers, hardcodedTokens).size,
            "nullableStructures" to nullableStructures(files).size,
        )
    }

    private fun levelOf(name: String, root: String): Int? {
        if (!name.startsWith("$root.")) return null
        val first = name.removePrefix("$root.").substringBefore('.')
        return when {
            first in particleLayers -> 0
            first in levels -> levels.getValue(first)
            else -> null
        }
    }

    private fun publicLines(files: List<SourceFile>): List<Triple<SourceFile, Int, String>> =
        files.flatMap { file ->
            file.lines.withIndex()
                .filterNot { (_, line) -> line.isCommentOrImport() || line.trimStart().startsWithHidden() }
                .map { (index, line) -> Triple(file, index, line) }
        }

    private fun String.startsWithHidden(): Boolean =
        startsWith("private") || startsWith("internal") || startsWith("@file")

    private fun String.isCommentOrImport(): Boolean {
        val trimmed = trimStart()
        return trimmed.startsWith("//") || trimmed.startsWith("*") || trimmed.startsWith("/*") || startsWith("import ")
    }

    private fun stripParentheses(text: String): String {
        val out = StringBuilder()
        var depth = 0
        for (ch in text) {
            when {
                ch == '(' -> { if (depth == 0) out.append('('); depth++ }
                ch == ')' -> { depth--; if (depth == 0) out.append(')') }
                depth == 0 -> out.append(ch)
            }
        }
        return out.toString()
    }

    private const val HEADER_LIMIT = 600
}
