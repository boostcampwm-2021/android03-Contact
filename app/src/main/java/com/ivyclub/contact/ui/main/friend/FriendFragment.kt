package com.ivyclub.contact.ui.main.friend

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.DialogFriendBinding
import com.ivyclub.contact.databinding.FragmentFriendBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.changeVisibilityWithDirection
import com.ivyclub.contact.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendFragment : BaseFragment<FragmentFriendBinding>(R.layout.fragment_friend) {

    private val viewModel: FriendViewModel by viewModels()
    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isSearchViewVisible.value == true) {
                    viewModel.setSearchViewVisibility()
                } else {
                    requireActivity().finish()
                }
            }
        }
    }
    private lateinit var friendListAdapter: FriendListAdapter
    private lateinit var dialog: Dialog
    private val dialogBinding by lazy { DialogFriendBinding.inflate(LayoutInflater.from(context)) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        initBackPressedCallback()
        initDialog()
        initAddButton()
        initSettingsButton()
        initFriendListAdapter()
        observeSearchViewVisibility()
        observeFriendList()
        viewModel.getFriendData()
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }

    private fun initBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun initAddButton() = with(binding) {
        ivAddFriendIcon.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            val menuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.menu_friend_and_group, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.item_new_friend -> {
                        findNavController().navigate(R.id.action_navigation_friend_to_addFriendFragment)
                    }
                    R.id.item_new_group -> {
                        showDialog()
                    }
                }
                false
            }
            popupMenu.show()
        }
    }

    private fun initSettingsButton() = with(binding) {
        ivSettingsIcon.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_friend_to_settingsFragment)
        }
    }

    private fun initDialog() {
        dialog = Dialog(requireContext())

        if (dialogBinding.root.parent == null) {
            dialog.setContentView(dialogBinding.root)
        }

        val layoutParams = dialog.window?.attributes
        layoutParams?.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        layoutParams?.height = ConstraintLayout.LayoutParams.WRAP_CONTENT

        with(dialogBinding) {
            friendViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            btnAddNewGroup.setOnClickListener {
                val groupName = etNewGroupName.text.toString()
                viewModel.saveGroupData(groupName)
                dialog.dismiss()
            }
        }
    }

    private fun showDialog() {
        viewModel.getGroupData()
        dialogBinding.etNewGroupName.text?.clear()
        dialog.show()
    }

    private fun initFriendListAdapter() {
        friendListAdapter = FriendListAdapter()
        binding.rvFriendList.adapter = friendListAdapter
    }

    private fun observeSearchViewVisibility() {
        viewModel.isSearchViewVisible.observe(viewLifecycleOwner) { newVisibilityState ->
            with(binding) {
                if (newVisibilityState) {
                    etSearch.changeVisibilityWithDirection(
                        Gravity.TOP,
                        View.VISIBLE,
                        ANIMATION_TIME
                    )
                } else {
                    hideKeyboard()
                    etSearch.changeVisibilityWithDirection(
                        Gravity.TOP,
                        View.GONE,
                        ANIMATION_TIME
                    )
                    ivRemoveEt.visibility = View.GONE
                }
            }
        }
    }

    private fun observeFriendList() {
        viewModel.friendList.observe(viewLifecycleOwner) { newFriendList ->
            // 새로운 리스트로 리사이클러뷰 갱신
            friendListAdapter.submitList(newFriendList) {
                binding.rvFriendList.scrollToPosition(0)
            }
        }
    }

    companion object {
        private const val ANIMATION_TIME = 150L
    }
}