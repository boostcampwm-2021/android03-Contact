package com.ivyclub.contact.ui.main.add_edit_plan

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentAddEditPlanBinding
import com.ivyclub.contact.ui.main.MainViewModel
import com.ivyclub.contact.ui.main.friend.dialog.SelectGroupFragment
import com.ivyclub.contact.util.*
import com.ivyclub.data.model.SimpleFriendData
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddEditPlanFragment :
    BaseFragment<FragmentAddEditPlanBinding>(R.layout.fragment_add_edit_plan) {

    private val viewModel: AddEditPlanViewModel by viewModels()
    private val args: AddEditPlanFragmentArgs by navArgs()
    private var currentBitmap: Bitmap? = null

    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showBackPressedDialog()
            }
        }
    }
    private val activityViewModel: MainViewModel by activityViewModels()
    private val filterActivityLauncher: ActivityResultLauncher<Intent> = // todo 가져오는 것부터 해야 함
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            activityViewModel.unlock()
            if (activityResult.resultCode == Activity.RESULT_OK && activityResult.data != null) {
                val currentImageUri = activityResult.data?.data
                val selectedImageCount = activityResult.data?.clipData?.itemCount ?: 0
                val tempList = mutableListOf<Uri>()
                for (idx in 0 until selectedImageCount) {
                    tempList.add(activityResult.data?.clipData?.getItemAt(idx)?.uri ?: continue)
                }
                Log.e("temptemp", ".${tempList}")
                viewModel.setPlanImageUri(tempList)
                try {
                    currentImageUri?.let {
                        activity?.let {
                            currentBitmap = if (Build.VERSION.SDK_INT < 28) {
                                val bitmap = MediaStore.Images.Media.getBitmap(
                                    it.contentResolver,
                                    currentImageUri
                                )
                                bitmap
                            } else {
                                val source =
                                    ImageDecoder.createSource(it.contentResolver, currentImageUri)
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                bitmap
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (activityResult.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, "사진 선택 취소", Toast.LENGTH_LONG).show()
            } else {
                Log.d("ActivityResult", "something wrong")
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.dateFormat =
            SimpleDateFormat(getString(R.string.format_simple_date), Locale.getDefault())

        initPhotoAdapter()
        initAddPhotoBtn()
        initBackPressedCallback()
        checkFrom()
        setButtonClickListeners()
        getGroupSelectFragmentResult()
        setObservers()
    }

    override fun onDetach() {
        onBackPressedCallback.remove()
        super.onDetach()
    }

    private fun initAddPhotoBtn() {
        binding.btnAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            filterActivityLauncher.launch(intent)
        }
    }

    private fun initPhotoAdapter() {
        val photoAdapter = PhotoAdapter()
        photoAdapter.submitList(
            listOf(
                PhotoData(""),
                PhotoData(""),
                PhotoData(""),
                PhotoData(""),
                PhotoData(""),
                PhotoData(""),
            )
        )
        binding.rvPhoto.adapter = photoAdapter
    }

    private fun initBackPressedCallback() {
        activity?.onBackPressedDispatcher?.addCallback(this, onBackPressedCallback)
    }

    private fun setButtonClickListeners() {
        with(binding) {
            ivBtnBack.setOnClickListener { showBackPressedDialog() }
            ivBtnEditPlanFinish.setOnClickListener {
                showSavePlanDialog()
            }
            tvPlanTime.setOnClickListener {
                this@AddEditPlanFragment.viewModel.planTime.value?.let {
                    showDatePickerDialog(it)
                }
            }
            tvBtnLoadGroup.setOnClickListener {
                SelectGroupFragment().show(
                    childFragmentManager,
                    SelectGroupFragment.TAG
                )
            }
        }
    }

    private fun showDatePickerDialog(date: Date) {
        if (context == null) return

        DatePickerDialog(
            requireContext(),
            { _, y, m, d ->
                showTimePickerDialog(date.getNewDate(y, m, d))
            }, date.getExactYear(), date.getExactMonth() - 1, date.getDayOfMonth()
        ).show()
    }

    private fun showTimePickerDialog(date: Date) {
        if (context == null) return

        TimePickerDialog(
            requireContext(),
            { _, h, m -> viewModel.setNewDate(date.getNewTime(h, m)) },
            date.getHour(), date.getMinute(), false
        ).show()
    }

    private fun showSavePlanDialog() {
        context?.showAlertDialog(getString(R.string.ask_save_plan), {
            viewModel.savePlan()
        })
    }

    private fun showBackPressedDialog() {
        context?.showAlertDialog(getString(R.string.ask_back_while_edit), {
            viewModel.finish()
        })
    }

    private fun checkFrom() {
        if (args.planId != -1L) viewModel.getLastPlan(args.planId)
        if (args.friendId != -1L) {
            viewModel.addFriend(args.friendId)
        }
    }

    private fun getGroupSelectFragmentResult() {
        childFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            val result = bundle.getLong("bundleKey", -1L)
            viewModel.addParticipantsByGroup(result)
        }
    }

    private fun setObservers() {
        viewModel.friendList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty() && binding.actPlanParticipants.adapter == null) {
                setAutoCompleteAdapter(it)
            }
        }

        viewModel.planParticipants.observe(viewLifecycleOwner) {
            binding.flPlanParticipants.addChips(it.map { pair -> pair.name }) { index ->
                viewModel.removeParticipant(index)
            }
        }

        viewModel.snackbarMessage.observe(viewLifecycleOwner) {
            if (context == null) return@observe
            Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_SHORT).show()
        }

        viewModel.finishEvent.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun setAutoCompleteAdapter(friendList: List<SimpleFriendData>) {
        if (context == null) return
        val autoCompleteAdapter = FriendAutoCompleteAdapter(requireContext(), friendList)
        with(binding.actPlanParticipants) {
            setOnItemClickListener { _, _, i, _ ->
                (adapter as FriendAutoCompleteAdapter).getItem(i)?.let {
                    viewModel.addParticipant(it)
                    text = null
                }
            }

            setAdapter(autoCompleteAdapter)
        }
    }
}