package com.yuriisurzhykov.pointdetector.presentation.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.designsystem.theme.PdTheme

/**
 * Shown over a screen that needs the permissions after they were refused: the same page as on
 * the first start. It asks again, or leads to the settings once the system stops asking.
 */
class PermissionDenialFragment : AbstractPermissionFragment() {

    private var step by mutableStateOf(PermissionStep.Request)

    private val actions = object : PermissionActions {
        override fun onGrant() = requestPermissions()
        override fun onOpenSettings() = openSettingsScreen()
        override fun onContinue() = removeCurrentFragment()
    }

    override fun getPermissionsArray(): Array<String> = requireArguments().getStringArray(ARG_PERMISSIONS)?.copyOf() ?: emptyArray()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            PdTheme { PermissionScreen(step, actions) }
        }
    }

    override fun onStart() {
        super.onStart()
        toolbar()?.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        toolbar()?.visibility = View.VISIBLE
    }

    override fun onPermissionsGranted() {
        removeCurrentFragment()
    }

    override fun onPermissionsDenied(shouldShowRationale: Boolean) {
        step = PermissionStep.afterDenial(shouldShowRationale)
    }

    /** The page draws its own bars under the system ones, so the shared toolbar is hidden. */
    private fun toolbar(): View? = activity?.findViewById(R.id.toolbar)

    companion object {
        private const val ARG_PERMISSIONS = "permissions"

        fun newInstance(permissions: Array<String>) = PermissionDenialFragment().apply {
            arguments = Bundle().apply { putStringArray(ARG_PERMISSIONS, permissions) }
        }
    }
}
