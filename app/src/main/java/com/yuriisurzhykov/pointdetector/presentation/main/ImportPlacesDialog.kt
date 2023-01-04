package com.yuriisurzhykov.pointdetector.presentation.main

import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnCancelListener
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.yuriisurzhykov.pointdetector.R

class ImportPlacesDialog : DialogFragment() {

    private var cancelListener: OnCancelListener? = null
    private var onOkClickListener: OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setMessage(R.string.message_dialog_import_places)
            .setPositiveButton(android.R.string.ok, onOkClickListener)
            .setTitle(R.string.title_dialog_import_data)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setNegativeButton(android.R.string.cancel) { _, _ -> }.setCancelable(true).create()
    }

    override fun onCancel(dialog: DialogInterface) {
        cancelListener?.onCancel(dialog)
        super.onCancel(dialog)
    }

    companion object {
        fun create(cancelListener: OnCancelListener?, okClickListener: OnClickListener) =
            ImportPlacesDialog().apply {
                this.cancelListener = cancelListener
                this.onOkClickListener = okClickListener
            }
    }

}