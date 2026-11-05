package com.yuriisurzhykov.pointdetector.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoFileDeclaration
import java.io.File

/** Konsist scopes of the project as [SourceFile]s with paths relative to the repository root. */
object ProjectSources {

    private val root: String = File(System.getProperty("user.dir")).parentFile.canonicalPath.replace('\\', '/')

    val production: List<SourceFile> by lazy { Konsist.scopeFromProduction().files.map(::toSource) }

    /** Tests of the app modules; this module's own fixtures contain violations on purpose. */
    val tests: List<SourceFile> by lazy {
        Konsist.scopeFromTest().files.map(::toSource).filterNot { it.path.startsWith("konsist/") }
    }

    /** The theme, `:designsystem`. */
    val theme: List<SourceFile> by lazy { production.filter { it.path.startsWith(DESIGN_SYSTEM_PATH) } }

    /** The Compose components in `:ui-components`; the old View code of that module is not covered. */
    val components: List<SourceFile> by lazy {
        production.filter { file ->
            file.path.startsWith(COMPONENTS_PATH) && COMPONENT_PACKAGES.any { "/uicomponents/$it/" in file.path }
        }
    }

    /** The whole design system: the theme and the components. */
    val designSystem: List<SourceFile> by lazy { theme + components }

    val toast: List<SourceFile> by lazy { production.filter { it.path.startsWith(TOAST_PATH) } }

    /**
     * Code that must take every metric, colour and timing from `PdTheme`. Token files hold the
     * literals: the theme, the icon paths (asset data) and the toast's own token file.
     */
    val tokenConsumers: List<SourceFile> by lazy {
        (designSystem + toast).filterNot {
            it.path.contains("/designsystem/theme/") ||
                it.path.contains("/uicomponents/icons/") ||
                it.path.contains("/toast/tokens/")
        }
    }

    /** App code written in Compose: must style itself only through the design system. */
    val appCompose: List<SourceFile> by lazy {
        production.filter { it.path.startsWith("app/") && it.text.contains("@Composable") }
    }

    const val DESIGN_SYSTEM_ROOT = "com.yuriisurzhykov.pointdetector.designsystem"
    const val COMPONENTS_ROOT = "com.yuriisurzhykov.pointdetector.uicomponents"
    const val TOAST_ROOT = "com.yuriisurzhykov.pointdetector.toast"
    const val APP_ROOT = "com.yuriisurzhykov.pointdetector"

    private const val DESIGN_SYSTEM_PATH = "designsystem/src/main/"
    private const val COMPONENTS_PATH = "ui-components/src/main/"
    private val COMPONENT_PACKAGES = listOf("atoms", "molecules", "organisms", "component", "resolver", "icons", "model")
    private const val TOAST_PATH = "toast/src/main/"

    private fun toSource(file: KoFileDeclaration): SourceFile =
        SourceFile(path = file.path.replace('\\', '/').removePrefix("$root/"), text = file.text)
}
