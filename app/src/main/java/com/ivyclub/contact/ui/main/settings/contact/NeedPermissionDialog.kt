package com.ivyclub.contact.ui.main.settings.contact

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentDialogRequestPermissionBinding
import android.content.Intent
import android.net.Uri
import android.provider.Settings


class NeedPermissionDialog : DialogFragment() {
    private lateinit var binding: FragmentDialogRequestPermissionBinding
    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_dialog_request_permission,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnConfirm.setOnClickListener {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", requireContext().packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }
    }

    companion object {
        const val TAG = "NEED_PERMISSION_DIALOG"
    }
}