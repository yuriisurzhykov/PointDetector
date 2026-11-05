package com.yuriisurzhykov.pointdetector.toast.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuriisurzhykov.pointdetector.designsystem.atoms.texts.PdPlainText
import com.yuriisurzhykov.pointdetector.designsystem.molecules.buttons.PdOutlineButton
import com.yuriisurzhykov.pointdetector.designsystem.molecules.buttons.PdPrimaryButton
import com.yuriisurzhykov.pointdetector.toast.ToastHostState
import com.yuriisurzhykov.pointdetector.toast.model.ToastType
import com.yuriisurzhykov.pointdetector.toast.ui.PdStatusToastBanner
import com.yuriisurzhykov.pointdetector.toast.ui.PdSwipeToastHost
import com.yuriisurzhykov.pointdetector.toast.ui.SwipeDismissBanner
import kotlinx.coroutines.launch

private val HOST_AREA_HEIGHT = 220.dp

@ToastPreviews
@Composable
private fun PdStatusToastBannerPreview() {
    ToastPreviewFrame {
        previewToastTypes.forEach { (name, type) ->
            PdStatusToastBanner(previewToast(name, type)).Content(Modifier)
        }
    }
}

@ToastPreviews
@Composable
private fun SwipeDismissBannerPreview() {
    ToastPreviewFrame {
        var shown by remember { mutableStateOf(true) }
        var type by remember { mutableStateOf<ToastType>(ToastType.Success) }
        PdPlainText(toastText("Swipe the banner up to dismiss it. The Error type is sticky and cannot be swiped.")).Content(Modifier)
        if (shown) {
            SwipeDismissBanner(
                data = previewToast(type::class.simpleName.orEmpty(), type),
                onDismiss = { shown = false },
            )
        }
        PdPrimaryButton(
            text = toastText("Show again as Success"),
            onClick = { type = ToastType.Success; shown = true },
        ).Content(Modifier)
        PdOutlineButton(
            text = toastText("Show again as Error (sticky)"),
            onClick = { type = ToastType.Error; shown = true },
        ).Content(Modifier)
    }
}

@ToastPreviews
@Composable
private fun PdSwipeToastHostPreview() {
    ToastPreviewFrame {
        val scope = rememberCoroutineScope()
        val hostState = remember { ToastHostState(scope) }
        var counter by remember { mutableIntStateOf(0) }
        PdPlainText(
            toastText("Show toasts: a more important one replaces the current one, an equal one waits in the queue."),
        ).Content(Modifier)
        Box(Modifier.fillMaxWidth().height(HOST_AREA_HEIGHT), contentAlignment = Alignment.TopCenter) {
            PdSwipeToastHost(hostState).Content(Modifier)
        }
        previewToastTypes.forEach { (name, type) ->
            PdOutlineButton(
                text = toastText("Show $name"),
                onClick = {
                    counter++
                    scope.launch { hostState.show(previewToast("$name #$counter", type)) }
                },
            ).Content(Modifier)
        }
        PdPrimaryButton(
            text = toastText("Dismiss the current toast"),
            onClick = { hostState.dismiss() },
        ).Content(Modifier)
    }
}
