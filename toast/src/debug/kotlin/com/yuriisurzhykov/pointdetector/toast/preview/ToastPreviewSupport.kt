package com.yuriisurzhykov.pointdetector.toast.preview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdLiteralText
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdTextResolver
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme
import com.yuriisurzhykov.pointdetector.toast.model.ToastData
import com.yuriisurzhykov.pointdetector.toast.model.ToastType

/**
 * Light, dark and large-font renders of a toast component. The design system has the same
 * annotation, but a debug source set cannot be shared between modules, so it is repeated here.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(name = "Light", widthDp = 360)
@Preview(name = "Dark", widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Large font", widthDp = 360, fontScale = 1.5f)
internal annotation class ToastPreviews

/** Theme, page background and padding around a previewed toast component. */
@Composable
internal fun ToastPreviewFrame(content: @Composable ColumnScope.() -> Unit) {
    PdTheme {
        val spacing = PdTheme.dimensions.spacing
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PdTheme.colors.surface.primary)
                .padding(spacing.regular),
            verticalArrangement = Arrangement.spacedBy(spacing.regular),
            content = content,
        )
    }
}

internal fun toastText(value: String): PdTextResolver = PdLiteralText(value)

internal val previewToastTypes: List<Pair<String, ToastType>> = listOf(
    "Info" to ToastType.Info,
    "Success" to ToastType.Success,
    "Warning" to ToastType.Warning,
    "Error" to ToastType.Error,
)

internal fun previewToast(name: String, type: ToastType): ToastData =
    ToastData(type = type, message = toastText("$name: the place was added to favorites"))
