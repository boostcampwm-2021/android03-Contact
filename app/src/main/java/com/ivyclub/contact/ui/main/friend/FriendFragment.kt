package com.ivyclub.contact.ui.main.friend

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        initBackPressedCallback()
        initClearButton()
        initAddButton()
        initFriendListAdapter()
        observeSearchTextChange()
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

    private fun initClearButton() = with(binding) {
        ivRemoveEt.setOnClickListener {
            etSearch.text.clear()
        }
    }

    private fun initAddButton() = with(binding) {
        ivAddFriendIcon.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            val menuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.menu_friend_and_group, popupMenu.menu)
            popupMenu.show()
        }
    }

    private fun initFriendListAdapter() {
        friendListAdapter = FriendListAdapter()
        friendListAdapter.setFriendList(viewModel.friendList.value?.toList() ?: emptyList())
        binding.rvFriendList.adapter = friendListAdapter
    }

    private fun observeSearchTextChange() {
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            if ((viewModel.isSearchViewVisible.value == true) && text.toString().isNotEmpty()) {
                binding.ivRemoveEt.visibility = View.VISIBLE
            } else {
                binding.ivRemoveEt.visibility = View.GONE
            }
        }
    }

    private fun observeSearchViewVisibility() {
        viewModel.isSearchViewVisible.observe(this) { newVisibilityState ->
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
                    etSearch.text.clear()
                    ivRemoveEt.visibility = View.GONE
                }
            }
        }
    }

    private fun observeFriendList() {
        viewModel.friendList.observe(this) { newFriendList ->
            // 새로운 리스트로 리사이클러뷰 갱신
            friendListAdapter.modifyFriendList(newFriendList.toList())
        }
    }

    companion object {
        private const val ANIMATION_TIME = 300L
    }
}