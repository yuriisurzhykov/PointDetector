package com.yuriisurzhykov.pointdetector.designsystem.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * One annotation, three renders of every component: light, dark and a large system font.
 * In Android Studio press "Interactive mode" on a card to click, toggle and swipe the component.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(name = "Light", widthDp = 360)
@Preview(name = "Dark", widthDp = 360, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Large font", widthDp = 360, fontScale = 1.5f)
annotation class PdPreviews
