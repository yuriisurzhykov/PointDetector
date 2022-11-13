package com.yuriisurzhykov.pointdetector.presentation.delegate

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty

class FragmentFindViewPropertyDelegate<in F : Fragment, out V : View> constructor(
    viewNeedInitialization: Boolean,
    viewMapper: (F) -> V,
    onDestroyView: (V) -> Unit = {}
) : FragmentReadOnlyPropertyDelegate<F, V>(viewNeedInitialization, viewMapper, onDestroyView)

fun <F : Fragment, V : View> Fragment.findView(
    @IdRes id: Int,
    viewProvider: () -> View = { requireView() }
): ReadOnlyProperty<F, V> {
    return internalFindViewById(id, viewProvider)
}

private fun <F : Fragment, V : View> Fragment.internalFindViewById(
    @IdRes resId: Int,
    viewProvider: () -> View
): ReadOnlyProperty<F, V> {
    return FragmentFindViewPropertyDelegate(true, { viewProvider.invoke().findViewById(resId) }) {}
}