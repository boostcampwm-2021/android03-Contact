package com.ivyclub.contact.ui.main.friend

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentFriendBinding
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.changeVisibilityWithDirection
import com.ivyclub.contact.util.hideKeyboard

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        initBackPressedCallback()
        initDialog()
        initAddButton()
        initFriendListAdapter()
        observeSearchViewVisibility()
        observeFriendList()
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

                    }
                    R.id.item_new_group -> {
                        dialog.show()
                    }
                }
                false
            }
            popupMenu.show()
        }
    }

    private fun initDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_friend)
        val layoutParams = dialog.window?.attributes
        layoutParams?.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        layoutParams?.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        // MVVM에 맞게 refactoring 필요
        dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }
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