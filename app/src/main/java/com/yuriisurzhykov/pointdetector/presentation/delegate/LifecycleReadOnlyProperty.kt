package com.yuriisurzhykov.pointdetector.presentation.delegate

import android.os.Handler
import android.os.Looper
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KProperty

abstract class LifecycleReadOnlyProperty<in R : Any, out T : Any> constructor(
    private val valueBinder: (R) -> T,
    private val onViewDestroyed: (T) -> Unit
) : ReadOnlyClearProperty<R, T> {

    private var view: T? = null

    protected abstract fun getLifecycleOwner(thisRef: R): LifecycleOwner

    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        checkMainThread()
        view?.let { return it }

        if (!isViewInitialized(thisRef)) {
            error(viewNotInitializedReadableErrorMessage(thisRef))
        }

        runStrictModeChecks(thisRef)

        val lifecycle = getLifecycleOwner(thisRef).lifecycle

        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            view = null
            return valueBinder.invoke(thisRef)
        } else {
            val view = valueBinder.invoke(thisRef)
            lifecycle.addObserver(ClearOnDestroyLifecycleObserver(this))
            this.view = view
            return view
        }
    }

    private fun viewNotInitializedReadableErrorMessage(thisRef: R): String {
        return "View $thisRef must be initialized before getting value!"
    }

    @CallSuper
    @MainThread
    override fun clear() {
        checkMainThread()
        val view = view
        this.view = null
        if (view != null) {
            onViewDestroyed(view)
        }
    }

    open fun postClear() {
        if (!mainHandler.post { clear() }) {
            clear()
        }
    }

    protected open fun isViewInitialized(thisRef: R): Boolean = true

    private fun runStrictModeChecks(thisRef: R) {
        val lifecycle = getLifecycleOwner(thisRef).lifecycle
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            error(ERROR_ACCESS_AFTER_DESTROY)
        }
    }

    private class ClearOnDestroyLifecycleObserver(
        private val property: LifecycleReadOnlyProperty<*, *>
    ) : DefaultLifecycleObserver {

        override fun onCreate(owner: LifecycleOwner) {
            // Do nothing
        }

        override fun onStart(owner: LifecycleOwner) {
            // Do nothing
        }

        override fun onResume(owner: LifecycleOwner) {
            // Do nothing
        }

        override fun onPause(owner: LifecycleOwner) {
            // Do nothing
        }

        override fun onStop(owner: LifecycleOwner) {
            // Do nothing
        }

        @MainThread
        override fun onDestroy(owner: LifecycleOwner) {
            property.postClear()
        }
    }

    private companion object {
        private val mainHandler = Handler(Looper.getMainLooper())
        private const val ERROR_ACCESS_AFTER_DESTROY = "Cannot access for view after owner was destroyed!"
    }
}