package com.ivyclub.contact.ui.main.add_edit_plan

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
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

const val MAX_PHOTO_COUNT = 5

@AndroidEntryPoint
class AddEditPlanFragment :
    BaseFragment<FragmentAddEditPlanBinding>(R.layout.fragment_add_edit_plan) {

    private val viewModel: AddEditPlanViewModel by viewModels()
    private val args: AddEditPlanFragmentArgs by navArgs()
    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showBackPressedDialog()
            }
        }
    }
    private val activityViewModel: MainViewModel by activityViewModels()
    private val filterActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            activityViewModel.unlock()
            if (activityResult.resultCode == Activity.RESULT_OK && activityResult.data != null) {
                if (activityResult.data?.clipData != null) { // 사용자가 이미지 여러 개 선택했을 때
                    val originImageCount = viewModel.bitmapUriList.value?.size ?: 0
                    val selectedImageCount = activityResult.data?.clipData?.itemCount ?: 0
                    val imageUriList = mutableListOf<Uri>()
                    for (idx in 1..selectedImageCount) {
                        if (originImageCount + idx > MAX_PHOTO_COUNT) { // 사진은 다섯장까지만 추가 가능하도록 구현
                            binding.makeShortSnackBar(getString(R.string.add_edit_plan_fragment_over_five_pics))
                            break
                        }
                        imageUriList.add(
                            activityResult.data?.clipData?.getItemAt(idx - 1)?.uri ?: continue
                        )
                    }
                    viewModel.setPlanImageUri(imageUriList)
                } else { // 사용자가 이미지 하나 선택했을 때
                    val imageUri = activityResult.data?.data
                    viewModel.setPlanImageUri(listOf(imageUri ?: return@registerForActivityResult))
                    binding.tvPhotoCount.text = String.format(
                        requireContext().getString(R.string.add_edit_plan_fragment_image_count),
                        1,
                        MAX_PHOTO_COUNT
                    )
                }
            } else if (activityResult.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, "사진 선택 취소", Toast.LENGTH_LONG).show()
            } else {
                Log.e(this::class.simpleName, "ActivityResult Went Wrong")
            }
        }
    private lateinit var photoAdapter: PhotoAdapter
    private val bitmapList = mutableListOf<Bitmap>()

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
        observePlanPhotoList()
    }

    override fun onDetach() {
        onBackPressedCallback.remove()
        super.onDetach()
    }

    private fun initAddPhotoBtn() {
        binding.btnAddImage.setOnClickListener {
            if (binding.tvPhotoCount.text == "($MAX_PHOTO_COUNT/$MAX_PHOTO_COUNT)") {
                binding.makeShortSnackBar(getString(R.string.add_edit_plan_fragment_over_five_pics))
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            filterActivityLauncher.launch(intent)
        }
    }

    private fun observePlanPhotoList() {
        viewModel.bitmapUriList.observe(viewLifecycleOwner) { newUriList ->
            bitmapList.clear()
            bitmapList.addAll(newUriList.map { uri ->
                requireActivity().uriToBitmap(uri)
            })
            photoAdapter.submitList(newUriList)
        }
    }

    private fun initPhotoAdapter() {
        photoAdapter = PhotoAdapter(viewModel::deletePhotoAt)
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
            ivEditPlanTimeIcon.setOnClickListener {
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
            viewModel.savePlan(bitmapList)
        })
    }

    private fun showBackPressedDialog() {
        context?.showAlertDialog(getString(R.string.ask_back_while_edit), {
            viewModel.finish()
        })
    }

    private fun checkFrom() {
        if (args.planId != -1L) viewModel.getLastPlan(args.planId)
        if (args.friendId != -1L) viewModel.addFriend(args.friendId)
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