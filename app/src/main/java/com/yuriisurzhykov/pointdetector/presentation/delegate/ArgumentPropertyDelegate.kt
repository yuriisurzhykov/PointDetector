package com.yuriisurzhykov.pointdetector.presentation.delegate

import android.os.Build
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty

fun <F : Fragment> F.stringArgument(argName: String): ReadOnlyProperty<F, String> {
    return fragmentArguments({ requireArguments().getString(argName).orEmpty() })
}

fun <F : Fragment> F.longArgument(argName: String): ReadOnlyProperty<F, Long> {
    return fragmentArguments({ requireArguments().getLong(argName) })
}

fun <F : Fragment> F.doubleArgument(argName: String): ReadOnlyProperty<F, Double> {
    return fragmentArguments({ requireArguments().getDouble(argName) })
}

inline fun <F : Fragment, reified T : Any> F.parcelableArgument(argName: String): ReadOnlyProperty<F, T> {
    return fragmentArguments({
        if (Build.VERSION.SDK_INT >= 33) {
            requireArguments().getParcelable(argName, T::class.java)!!
        } else {
            requireArguments().getParcelable(argName)!!
        }
    })
}

inline fun <F : Fragment, reified T : Serializable> F.serializableArgument(argName: String): ReadOnlyProperty<F, T> {
    return fragmentArguments({
        if (Build.VERSION.SDK_INT >= 33) {
            requireArguments().getSerializable(argName, T::class.java)!!
        } else {
            requireArguments().getSerializable(argName) as T
        }
    })
}

fun <F : Fragment, T : Any> F.fragmentArguments(
    valueMapper: (Fragment) -> T,
    onDestroyView: (T) -> Unit = {}
): ReadOnlyProperty<F, T> {
    return ArgumentPropertyDelegate(valueMapper, onDestroyView)
}

fun <F : Fragment, T : Any> F.fragmentArguments(
    argName: String
): ReadOnlyProperty<F, T> {
    return ArgumentPropertyDelegate({
        requireArguments().get(argName) as T
    }, {})
}

class ArgumentPropertyDelegate<T : Any>(
    valueMapper: (Fragment) -> T,
    onDestroyView: (T) -> Unit
) : FragmentReadOnlyPropertyDelegate<Fragment, T>(
    viewNeedInitialization = false,
    valueMapper,
    onDestroyView
)