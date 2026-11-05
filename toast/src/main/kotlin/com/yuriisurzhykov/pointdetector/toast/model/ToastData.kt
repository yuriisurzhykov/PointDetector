package com.yuriisurzhykov.pointdetector.toast.model

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.designsystem.resolver.text.PdTextResolver
import java.util.UUID

/** What to show. The icon and colours come from the [type], the text from a resolver. */
@Immutable
data class ToastData(
    val id: String = UUID.randomUUID().toString(),
    val type: ToastType,
    val message: PdTextResolver,
    val duration: ToastDuration = type.defaultDuration,
)
