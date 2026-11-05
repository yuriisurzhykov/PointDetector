package com.yuriisurzhykov.pointdetector.presentation.messages

import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.domain.messages.AppMessage
import com.yuriisurzhykov.pointdetector.toast.model.ToastData
import com.yuriisurzhykov.pointdetector.toast.model.ToastDuration
import com.yuriisurzhykov.pointdetector.toast.model.ToastType
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private val SHORT: Duration = 2.seconds
private val LONG: Duration = 5.seconds

/**
 * Turns a message of the app into a toast: the type says how important it is (and so how it
 * is coloured and what it pre-empts), the duration says how long it stays. Errors and warnings
 * are timed too, so nothing stays on the screen until it is swiped away.
 */
class ToastMessages @Inject constructor() {

    fun toast(message: AppMessage): ToastData = when (message) {
        is AppMessage.NoConnection -> toast(ToastType.Warning, R.string.toast_no_connection, LONG)
        is AppMessage.SyncFailed -> toast(ToastType.Warning, R.string.toast_sync_failed, LONG)
        is AppMessage.PlaceAdded -> toast(ToastType.Success, R.string.label_point_successfully_created, LONG)
        is AppMessage.PlaceNotSaved -> toast(ToastType.Error, reason(message.reason), LONG)
        is AppMessage.PlaceDeleted -> toast(ToastType.Success, R.string.toast_place_deleted, SHORT)
        is AppMessage.PlaceNotDeleted -> toast(ToastType.Error, R.string.toast_place_not_deleted, LONG)
        is AppMessage.FavoriteAdded -> toast(ToastType.Info, R.string.toast_favorite_added, SHORT)
        is AppMessage.FavoriteRemoved -> toast(ToastType.Info, R.string.toast_favorite_removed, SHORT)
        is AppMessage.CityNotDetected -> toast(ToastType.Warning, R.string.city_auto_failed, LONG)
    }

    private fun reason(reason: AppMessage.PlaceNotSaved.Reason): Int = when (reason) {
        AppMessage.PlaceNotSaved.Reason.CityNotSelected -> R.string.error_city_not_selected
        AppMessage.PlaceNotSaved.Reason.AddressNotFound -> R.string.add_address_not_found
        AppMessage.PlaceNotSaved.Reason.Failed -> R.string.add_error_generic
    }

    private fun toast(type: ToastType, text: Int, duration: Duration) =
        ToastData(type = type, message = PdStringText(text), duration = ToastDuration.Timed(duration))
}
