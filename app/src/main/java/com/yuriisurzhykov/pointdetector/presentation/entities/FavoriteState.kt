package com.yuriisurzhykov.pointdetector.presentation.entities

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/** Whether a place is a favorite. */
@Polymorphic
interface FavoriteState : java.io.Serializable {

    @Serializable
    data object FavoriteEnabled : FavoriteState {
        private fun readResolve(): Any = FavoriteEnabled
    }

    @Serializable
    data object FavoriteDisabled : FavoriteState {
        private fun readResolve(): Any = FavoriteDisabled
    }

    object Factory {
        fun build(enabled: Boolean): FavoriteState {
            return if (enabled) FavoriteEnabled else FavoriteDisabled
        }
    }
}
