package com.yuriisurzhykov.pointdetector.presentation.entities

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes

interface FavoriteState {

    fun apply(imageView: ImageView)

    abstract class AbstractResourceState(@DrawableRes private val drawableId: Int) : FavoriteState {
        override fun apply(imageView: ImageView) {
            imageView.setImageResource(drawableId)
        }
    }

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
            if (visibility != other.visibility) return false
            return true
        }

        override fun hashCode(): Int {
            return visibility
        }
    }

    class FavoriteEnabled : AbstractVisibilityState(View.VISIBLE)
    class FavoriteDisabled : AbstractVisibilityState(View.GONE)

    object Factory {
        fun build(enabled: Boolean): FavoriteState {
            return if (enabled) FavoriteEnabled() else FavoriteDisabled()
        }
    }
}