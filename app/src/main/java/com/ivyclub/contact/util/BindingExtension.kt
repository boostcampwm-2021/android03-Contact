package com.ivyclub.contact.util

import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar

fun ViewDataBinding.makeShortSnackBar(content: String) {
    Snackbar.make(
        this.root,
        content,
        Snackbar.LENGTH_SHORT
    ).show()
}