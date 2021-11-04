package com.ivyclub.contact.ui.onboard.contact

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentAddContactBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.SkipDialog
import com.ivyclub.data.model.PhoneContactData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactFragment : BaseFragment<FragmentAddContactBinding>(R.layout.fragment_add_contact){

    private lateinit var contactAdapter: ContactAdapter
    private lateinit var contactList: MutableList<PhoneContactData>
    private val viewModel: AddContactViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactList = mutableListOf()
        initRecyclerView()
        initButtons()
        initAppBar()
    }

    private fun initRecyclerView() {
        contactAdapter = ContactAdapter()
        binding.rvContactList.adapter = contactAdapter
    }

    private fun initButtons() = with(binding) {
        btnLoad.setOnClickListener {
            requestPermission()
        }
        btnCommit.setOnClickListener {
            viewModel.savePeople(contactAdapter.addList)
            requireActivity().finish()
        }
        btnCommit.isClickable = false
    }

    private fun loadContact() {
        with(binding) {
            val buttonAnimation = AnimationUtils.loadAnimation(requireContext(),R.anim.button_down)
            val textAnimation = AnimationUtils.loadAnimation(requireContext(),R.anim.text_gone)
            val recyclerViewAnimation = AnimationUtils.loadAnimation(requireContext(),R.anim.recyclerview_fade_in)
            btnLoad.startAnimation(buttonAnimation)
            tvIntroduce.startAnimation(textAnimation)
            rvContactList.visibility = View.VISIBLE
            rvContactList.startAnimation(recyclerViewAnimation)
            btnLoad.isClickable = false
            btnLoad.text = "시작하기"
            contactAdapter.submitList(getContact())
            btnCommit.isClickable = true
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadContact()
        } else {
            requireActivity().finish()
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun initAppBar() {
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.tbAddContact.setupWithNavController(navController,appBarConfiguration)
        binding.tbAddContact.title = ""
        binding.tbAddContact.inflateMenu(R.menu.menu_on_boarding)
        binding.tbAddContact.setOnMenuItemClickListener {
            if(it.itemId == R.id.skip) {
                SkipDialog(ok,context).showDialog()
            }
            true
        }
    }
    private val ok = DialogInterface.OnClickListener { _, _ ->
        requireActivity().finish()
    }

}