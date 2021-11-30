package com.ivyclub.contact.ui.main.add_edit_friend

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentAddEditFriendBinding
import com.ivyclub.contact.ui.main.MainViewModel
import com.ivyclub.contact.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Date


@AndroidEntryPoint
class AddEditFriendFragment :
    BaseFragment<FragmentAddEditFriendBinding>(R.layout.fragment_add_edit_friend) {

    private val viewModel: AddEditFriendViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()
    val extraInfoListAdapter by lazy { ExtraInfoListAdapter(viewModel::removeExtraInfo) }
    private val args: AddEditFriendFragmentArgs by navArgs()
    lateinit var spinnerAdapter: ArrayAdapter<String>
    private var currentBitmap: Bitmap? = null
    private var newId: Long = -1L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.fragment = this
        binding.ivProfileImage.clipToOutline = true
        setObserver()
        setClickListener()
        setBackPressedListener()
        setPhoneNumberTextChangedListener()
    }

    private fun setFriendData() {
        if (args.friendId != -1L) {
            viewModel.getFriendData(args.friendId)
            viewModel.friendData.observe(viewLifecycleOwner) { friendData ->
                binding.apply {
                    etName.setText(friendData.name)
                    etPhoneNumber.setText(friendData.phoneNumber)
                    tvBirthdayValue.text = friendData.birthday
                    spnGroup.setSelection(this@AddEditFriendFragment.viewModel.groupIdList.indexOf(friendData.groupId))
                }
                viewModel.addExtraInfoList(friendData.extraInfo)
            }
            viewModel.loadProfileImage(args.friendId)?.let { binding.ivProfileImage.setImageBitmap(it) }
        } else {
            viewModel.createNewId()
        }
    }

    private fun setObserver() {
        viewModel.groupNameList.observe(viewLifecycleOwner) {
            initSpinnerAdapter(it)
            setFriendData()
        }

        viewModel.isSaveButtonClicked.observe(viewLifecycleOwner) {
            with(binding) {
                this@AddEditFriendFragment.viewModel.saveFriendData(
                    etPhoneNumber.text.toString(),
                    etName.text.toString(),
                    tvBirthdayValue.text.toString(),
                    this@AddEditFriendFragment.viewModel.groupIdList[spnGroup.selectedItemPosition],
                    extraInfoListAdapter.currentList,
                    args.friendId
                )
                if (args.friendId != -1L) {
                    this@AddEditFriendFragment.viewModel.saveProfileImage(
                        currentBitmap,
                        args.friendId
                    )
                } else {
                    this@AddEditFriendFragment.viewModel.saveProfileImage(currentBitmap, newId)
                }
                findNavController().popBackStack()
                Snackbar.make(
                    binding.root,
                    getString(R.string.add_edit_success_message),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.extraInfos.observe(viewLifecycleOwner) {
            extraInfoListAdapter.submitList(it.toMutableList())
        }

        viewModel.newId.observe(viewLifecycleOwner) {
            newId = it
        }

        viewModel.nameValidation.observe(viewLifecycleOwner) {
            binding.tvNameValidCheck.text = getString(it)
        }
    }

    private fun setClickListener() {
        with(binding) {
            ivBackIcon.setOnClickListener {
                showBackPressedDialog()
            }

            tvBirthdayValue.setOnClickListener {
                val today = Date(System.currentTimeMillis())
                val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    tvBirthdayValue.text = "${year}.${month + 1}.${day}"
                    this@AddEditFriendFragment.viewModel.showClearButtonVisible(true)
                }
                DatePickerDialog(
                    requireContext(),
                    listener,
                    today.getExactYear(),
                    today.getExactMonth() - 1,
                    today.getDayOfMonth()
                ).show()
            }

            ivClearBirthday.setOnClickListener {
                tvBirthdayValue.text = ""
                this@AddEditFriendFragment.viewModel.showClearButtonVisible(false)
            }

            ivProfileImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("image/*")
                filterActivityLauncher.launch(intent)
            }
        }
    }

    private fun setPhoneNumberTextChangedListener() {
        binding.etPhoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())
    }

    private val filterActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            activityViewModel.unlock()
            if (activityResult.resultCode == RESULT_OK && activityResult.data != null) {
                val currentImageUri = activityResult.data?.data
                try {
                    currentImageUri?.let {
                        activity?.let {
                            if (Build.VERSION.SDK_INT < 28) {
                                val bitmap = MediaStore.Images.Media.getBitmap(
                                    it.contentResolver,
                                    currentImageUri
                                )
                                binding.ivProfileImage.setImageBitmap(bitmap)
                                currentBitmap = bitmap
                            } else {
                                val source =
                                    ImageDecoder.createSource(it.contentResolver, currentImageUri)
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                binding.ivProfileImage.setImageBitmap(bitmap)
                                currentBitmap = bitmap
                            }
                        }

                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (activityResult.resultCode == RESULT_CANCELED) {
                Toast.makeText(context, "사진 선택 취소", Toast.LENGTH_LONG).show();
            } else {
                Log.d("ActivityResult", "something wrong")
            }
        }

    private fun showBackPressedDialog() {
        if (context == null) return
        requireContext().showAlertDialog(getString(R.string.ask_back_while_edit), {
            findNavController().popBackStack()
        })
    }

    private fun setBackPressedListener() {
        if (activity == null) return
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showBackPressedDialog()
        }
    }

    private fun initSpinnerAdapter(groups: List<String>) {
        if (context == null) return
        spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groups)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnGroup.adapter = spinnerAdapter
    }
}