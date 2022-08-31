package com.yuriisurzhykov.pointdetector.presentation.permissions

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.core.TAG

class PermissionDenialFragment : AbstractPermissionFragment(R.layout.fragment_info) {

    private var dataSource: PermissionResourceManager? = null

    override fun getPermissionsArray(): Array<String> {
        return dataSource?.getPermissionsArray()!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context
        view.findViewById<TextView>(R.id.title).text = dataSource?.getInfoTitle(context)
        view.findViewById<TextView>(R.id.rationale).text = dataSource?.getDescriptionRationaleText(context)
        view.findViewById<ImageView>(R.id.image)
            .setImageDrawable(dataSource?.getInfoImage(context))
        with(view.findViewById<Button>(R.id.retry)) {
            text = dataSource?.getRetryButtonText(context)
            setOnClickListener { requestPermissions() }
        }
    }

    override fun onPermissionsGranted() {
        dataSource?.onPermissionsGranted()
    }

    override fun onPermissionsDenied(shouldShowRationale: Boolean) {
        Log.e(TAG, "onPermissionsDenied: ")
    }

    companion object {
        fun newInstance(source: PermissionResourceManager): PermissionDenialFragment {
            val fragment = PermissionDenialFragment()
            fragment.dataSource = source
            return fragment
        }
    }
}