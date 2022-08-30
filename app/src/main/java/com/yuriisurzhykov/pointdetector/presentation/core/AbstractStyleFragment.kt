package com.yuriisurzhykov.pointdetector.presentation.core

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.yuriisurzhykov.pointdetector.R

abstract class AbstractStyleFragment : AbstractFragment, IStyledFragment {
    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun getTitle(): CharSequence {
        return resources.getString(R.string.app_name)
    }

}