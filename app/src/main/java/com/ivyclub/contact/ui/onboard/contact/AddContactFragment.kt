package com.ivyclub.contact.ui.onboard.contact

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentAddContactBinding
import com.ivyclub.contact.ui.MainActivity
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.data.model.PhoneContactData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactFragment : BaseFragment<FragmentAddContactBinding>(R.layout.fragment_add_contact) {

    private lateinit var contactAdapter: ContactAdapter
    private lateinit var contactList: MutableList<PhoneContactData>
    private val viewModel: AddContactViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactList = mutableListOf()
        initRecyclerView()
        initButtons()
    }

    private fun initRecyclerView() {
        contactAdapter = ContactAdapter()
        binding.rvContactList.adapter = contactAdapter
    }

    private fun initButtons() {
        binding.button.setOnClickListener {
            val buttonAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.button_down)
            val textAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_gone)
            val recyclerViewAnimation =
                AnimationUtils.loadAnimation(requireContext(), R.anim.recyclerview_fade_in)
            binding.button.startAnimation(buttonAnimation)
            binding.textView.startAnimation(textAnimation)
            binding.rvContactList.visibility = View.VISIBLE
            binding.rvContactList.startAnimation(recyclerViewAnimation)
            binding.button.isClickable = false
            binding.button.text = "시작하기"
            contactAdapter.submitList(getContact())
        }
        binding.btnCommit.setOnClickListener {
            viewModel.savePeople(contactAdapter.addList)
            val intent = Intent(requireActivity(), MainActivity::class.java)
            requireActivity().setResult(RESULT_OK, intent)
            requireActivity().finish()
        }
    }

    @SuppressLint("Range")
    fun getContact(): MutableList<PhoneContactData> {
        val contactList: MutableList<PhoneContactData> = mutableListOf()
        val contacts = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        while (contacts!!.moveToNext()) {
            val name =
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number =
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val obj = PhoneContactData(name, number)
            contactList.add(obj)
        }
        contacts.close()
        return contactList
    }
}