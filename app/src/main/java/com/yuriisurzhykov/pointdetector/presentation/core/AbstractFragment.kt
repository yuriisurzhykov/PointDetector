package com.yuriisurzhykov.pointdetector.presentation.core

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.lang.IllegalStateException

abstract class AbstractFragment : Fragment, NavigationCallback {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    private var navigationCallback: NavigationCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is NavigationCallback) {
            throw IllegalStateException("Passed context must implement NavigationCallback!")
        }
        navigationCallback = context
    }

    override fun onDetach() {
        super.onDetach()
        navigationCallback = null
    }

    override fun openFragment(fragment: Fragment, tag: String) {
        navigationCallback?.openFragment(fragment, tag)
    }

    override fun openFragment(fragment: Fragment, tag: String, removeCurrentFragment: Boolean) {
        navigationCallback?.openFragment(fragment, tag, removeCurrentFragment)
    }

    override fun removeCurrentFragment() {
        navigationCallback?.removeCurrentFragment()
    }

    override fun openMainFragment() {
        navigationCallback?.openMainFragment()
    }

    protected fun showErrorSnackbar(message: String) {
        Snackbar
            .make(requireView(), message, Snackbar.LENGTH_SHORT)
            .apply {
                view.setBackgroundColor(Color.RED)
                setActionTextColor(Color.WHITE)
                show()
            }
    }
}