package com.ivyclub.contact.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.ivyclub.contact.R

class SkipDialog(
    private val ok: DialogInterface.OnClickListener,
    private val context: Context?
) {
    fun showDialog() {
        AlertDialog.Builder(context)
            .setTitle(context?.getString(R.string.menu_skip) ?: "Skip")
            .setMessage(
                context?.getString(R.string.skip_dialog_seriously_end_question)
                    ?: "Do you want to quit?"
            )
            .setPositiveButton(context?.getString(R.string.skip_dialog_yes) ?: "yes", ok)
            .setNegativeButton(
                context?.getString(R.string.skip_dialog_no) ?: "no"
            ) { _, _ -> }
            .create()
            .show()
    }
}