package com.yuriisurzhykov.pointdetector.uicomponents.centertoolbar

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar

class CenteredTextToolbar : Toolbar {

    private var centeredTitleTextView: TextView? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setTitle(@StringRes resId: Int) {
        val s = resources.getString(resId)
        this.title = s
    }

    override fun setTitle(title: CharSequence?) {
        getCenteredTitleTextView()?.text = title
    }

    override fun getTitle(): CharSequence {
        return getCenteredTitleTextView()?.text.toString()
    }

    private fun getCenteredTitleTextView(): TextView? {
        if (centeredTitleTextView == null) {
            centeredTitleTextView = TextView(context).apply {
                setSingleLine()
                ellipsize = TextUtils.TruncateAt.END
                gravity = Gravity.CENTER
                setTextAppearance(
                    androidx.appcompat.R.style.TextAppearance_AppCompat_Widget_ActionBar_Title
                )
                val toolbarLayoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
                toolbarLayoutParams.gravity = Gravity.CENTER
                layoutParams = toolbarLayoutParams
            }
            addView(centeredTitleTextView)
        }
        return centeredTitleTextView
    }

}