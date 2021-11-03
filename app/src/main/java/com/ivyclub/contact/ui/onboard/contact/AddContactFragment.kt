package com.ivyclub.contact.ui.onboard.contact

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentAddContactBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.data.model.PhoneContactData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactFragment : BaseFragment<FragmentAddContactBinding>(R.layout.fragment_add_contact){

    private lateinit var contactAdapter: ContactAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactAdapter = ContactAdapter()
        binding.rvContactList.adapter = contactAdapter
        initButtons()

    }

    private fun initButtons() {
        binding.button.setOnClickListener {
            val ani1 = AnimationUtils.loadAnimation(requireContext(),R.anim.button_down)
            val ani2 = AnimationUtils.loadAnimation(requireContext(),R.anim.text_gone)
            val ani3 = AnimationUtils.loadAnimation(requireContext(),R.anim.recyclerview_fade_in)
            binding.button.startAnimation(ani1)
            binding.textView.startAnimation(ani2)
            binding.rvContactList.visibility = View.VISIBLE
            binding.rvContactList.startAnimation(ani3)
            binding.button.isClickable = false
            contactAdapter.submitList(getContact())
        }
    }

    @SuppressLint("Range")
    fun getContact(): MutableList<PhoneContactData>{
        val contactList: MutableList<PhoneContactData> = mutableListOf()
        val contacts = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        while (contacts!!.moveToNext()){
            val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val obj = PhoneContactData(name, number)

            contactList.add(obj)
        }
        contacts.close()
        return contactList
    }
}