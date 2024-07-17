package com.yuriisurzhykov.pointdetector.uicomponents.drawable

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap

interface DrawableScale {
    fun scale(drawable: Drawable): Drawable

    abstract class Abstract(
        private val resources: Resources
    ) : DrawableScale {

        protected abstract fun madeBitmap(drawable: Drawable): Bitmap

        protected abstract fun makeBitmapSize(bitmap: Bitmap): Pair<Int, Int>

        override fun scale(drawable: Drawable): Drawable {
            val bitmap = madeBitmap(drawable)
            val sizes = makeBitmapSize(bitmap)
            val resizedBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, sizes.first, sizes.second, false)
            return BitmapDrawable(resources, resizedBitmap)
        }
    }

    abstract class VectorDrawableResize(resources: Resources) : Abstract(resources) {
        override fun madeBitmap(drawable: Drawable): Bitmap {
            return drawable.toBitmap()
        }
    }

    abstract class AspectRatioResize constructor(
        resources: Resources,
        private val ratioScaleFactor: Float
    ) : VectorDrawableResize(resources) {

        override fun makeBitmapSize(bitmap: Bitmap): Pair<Int, Int> {
            return Pair((bitmap.height * ratioScaleFactor).toInt(), (bitmap.width * ratioScaleFactor).toInt())
        }
    }
}