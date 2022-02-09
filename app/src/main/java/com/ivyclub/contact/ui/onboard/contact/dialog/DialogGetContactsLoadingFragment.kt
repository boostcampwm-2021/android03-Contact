package com.ivyclub.contact.ui.onboard.contact.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentDialogGetContactsLoadingBinding

class DialogGetContactsLoadingFragment : DialogFragment() {
    private lateinit var binding: FragmentDialogGetContactsLoadingBinding
    private var isShowing = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        isShowing = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dialog_get_contacts_loading,
            container,
            false
        )
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        isShowing = false
    }

    fun getIsShowing() = isShowing

    companion object {
        const val TAG = "DIALOG_GET_CONTACTS_LOADING_FRAGMENT"
    }
}