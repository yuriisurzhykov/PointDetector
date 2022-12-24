package com.yuriisurzhykov.pointdetector.presentation.delegate

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

open class FragmentReadOnlyPropertyDelegate<in F : Fragment, out T : Any> constructor(
    private val viewNeedInitialization: Boolean = true,
    valueMapper: (F) -> T,
    onDestroyView: (T) -> Unit
) : LifecycleReadOnlyProperty<F, T>(valueMapper, onDestroyView) {

    private var fragmentLifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks? = null
    private var fragmentManager: WeakReference<FragmentManager>? = null

    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        return thisRef.viewLifecycleOwner
    }

    override fun getValue(thisRef: F, property: KProperty<*>): T {
        val view = super.getValue(thisRef, property)
        registerFragmentsLifecycleCallbacks(thisRef)
        return view
    }

    override fun isViewInitialized(thisRef: F): Boolean {
        return when {
            !viewNeedInitialization -> true
            !thisRef.isAdded || thisRef.isDetached -> false
            thisRef !is DialogFragment -> thisRef.view != null
            else -> super.isViewInitialized(thisRef)
        }
    }

    override fun clear() {
        super.clear()
        fragmentManager?.get()?.let { fragmentManager ->
            fragmentLifecycleCallbacks?.let { fragmentManager::unregisterFragmentLifecycleCallbacks }
        }
        fragmentManager = null
        fragmentLifecycleCallbacks = null
    }

    private fun registerFragmentsLifecycleCallbacks(fragment: F) {
        if (fragmentLifecycleCallbacks != null) return

        val fragmentManager = fragment.parentFragmentManager.also { fm ->
            this.fragmentManager = WeakReference(fm)
        }
        fragmentLifecycleCallbacks = ClearOnDestroy(fragment).also { callbacks ->
            fragmentManager.registerFragmentLifecycleCallbacks(callbacks, false)
        }
    }

    private inner class ClearOnDestroy(
        fragment: Fragment
    ) : FragmentManager.FragmentLifecycleCallbacks() {

        private var fragment: Reference<Fragment> = WeakReference(fragment)

        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
            // Fix for destroying view for case with issue of navigation
            if (fragment.get() === f) {
                postClear()
            }
        }
    }
}