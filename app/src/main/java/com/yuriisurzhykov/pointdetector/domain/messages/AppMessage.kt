package com.yuriisurzhykov.pointdetector.domain.messages

private const val QUIET_MILLIS = 1_000L
private const val LONG_QUIET_MILLIS = 30_000L

/**
 * Something the person should be told, as a plain fact with no text and no UI type in it: the
 * screens turn it into a toast. [quietMillis] is how long the same message is not repeated.
 */
sealed interface AppMessage {

    val quietMillis: Long get() = QUIET_MILLIS

    /** The phone has no network: the saved places are shown. */
    data object NoConnection : AppMessage {
        override val quietMillis: Long = LONG_QUIET_MILLIS
    }

    /** The network is there, but the places could not be updated: the saved places are shown. */
    data object SyncFailed : AppMessage {
        override val quietMillis: Long = LONG_QUIET_MILLIS
    }

    data class PlaceAdded(val name: String) : AppMessage

    data class PlaceNotSaved(val reason: Reason) : AppMessage {
        enum class Reason { CityNotSelected, AddressNotFound, Failed }
    }

    data object PlaceDeleted : AppMessage

    data object PlaceNotDeleted : AppMessage

    data object FavoriteAdded : AppMessage

    data object FavoriteRemoved : AppMessage

    /** "Detect automatically" found no city: no location, or no city near it. */
    data object CityNotDetected : AppMessage
}
