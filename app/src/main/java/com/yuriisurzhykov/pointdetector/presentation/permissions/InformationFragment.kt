package com.yuriisurzhykov.pointdetector.presentation.permissions

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.presentation.core.AbstractFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class InformationFragment : AbstractFragment(R.layout.fragment_info) {

    protected var dataSource: InfoResourceManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context
        view.findViewById<TextView>(R.id.title).text = dataSource?.getInfoTitle(context)
        view.findViewById<TextView>(R.id.rationale).text = dataSource?.getDescriptionRationaleText(context)
        view.findViewById<ImageView>(R.id.image)
            .setImageDrawable(dataSource?.getInfoImage(context))
        with(view.findViewById<Button>(R.id.retry)) {
            text = dataSource?.getRetryButtonText(context)
            setOnClickListener { onButtonClick(it) }
        }
    }

    protected open fun onButtonClick(view: View) {
        dataSource?.onButtonClick(requireActivity(), view)
    }

    companion object {
        fun newInstance(dataSource: InfoResourceManager): InformationFragment {
            val fragment = InformationFragment()
            fragment.dataSource = dataSource
            return fragment
        }
    }
}