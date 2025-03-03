package com.example.musicplayer.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.musicplayer.R

class ConfirmationDialogFragment(
    private val messageId: Int,
    private val listener: OnDeleteConfirmListener
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val title = getString(R.string.confirmation_title)
        builder.setIcon(R.drawable.ic_warning)
        builder.setTitle(title)
        builder.setMessage(messageId)
        builder.setPositiveButton(R.string.yes) { _, _ ->
            listener.onConfirm(true)
        }
        builder.setNegativeButton(R.string.no) { _, _ ->
            listener.onConfirm(false)
        }
        return builder.create()
    }

    interface OnDeleteConfirmListener {
        fun onConfirm(isConfirmed: Boolean)
    }

    companion object {
        const val TAG = "ConfirmationDialog"
    }
}