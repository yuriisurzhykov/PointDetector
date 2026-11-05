package com.yuriisurzhykov.pointdetector.uicomponents.molecules.notice

import androidx.compose.runtime.Immutable
import com.yuriisurzhykov.pointdetector.uicomponents.R
import com.yuriisurzhykov.pointdetector.uicomponents.icons.PdIcons
import com.yuriisurzhykov.pointdetector.uicomponents.resolver.text.PdStringText

/** The app works from the saved places because there is no connection. */
@Immutable
object PdOfflineNotice : PdWarningNotice(PdIcons.WifiOff, PdStringText(R.string.pd_offline_notice))
