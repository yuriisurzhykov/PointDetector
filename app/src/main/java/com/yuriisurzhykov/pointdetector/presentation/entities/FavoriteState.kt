package com.yuriisurzhykov.pointdetector.presentation.entities

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Polymorphic
interface FavoriteState : java.io.Serializable {

    fun apply(imageView: ImageView)

    @Suppress("unused")
    abstract class AbstractResourceState(@DrawableRes private val drawableId: Int) : FavoriteState {
        override fun apply(imageView: ImageView) {
            imageView.setImageResource(drawableId)
        }
    }

    @Serializable
    abstract class AbstractVisibilityState(private val visibility: Int) : FavoriteState {

        override fun apply(imageView: ImageView) {
            if (imageView.visibility != visibility) {
                imageView.visibility = visibility
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as AbstractVisibilityState
            return visibility == other.visibility
        }

        override fun hashCode(): Int {
            return visibility
        }
    }

    @Serializable
    data object FavoriteEnabled : AbstractVisibilityState(View.VISIBLE) {
        private fun readResolve(): Any = FavoriteEnabled
    }

    @Serializable
    data object FavoriteDisabled : AbstractVisibilityState(View.GONE) {
        private fun readResolve(): Any = FavoriteDisabled
    }

    object Factory {
        fun build(enabled: Boolean): FavoriteState {
            return if (enabled) FavoriteEnabled else FavoriteDisabled
        }
    }
}