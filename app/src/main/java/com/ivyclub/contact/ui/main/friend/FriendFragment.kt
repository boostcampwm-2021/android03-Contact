package com.ivyclub.contact.ui.main.friend

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentFriendBinding
import com.ivyclub.contact.ui.main.friend.dialog.AddGroupDialogFragment
import com.ivyclub.contact.ui.main.friend.dialog.SelectGroupFragment
import com.ivyclub.contact.util.BaseFragment
import com.ivyclub.contact.util.changeVisibilityWithDirection
import com.ivyclub.contact.util.hideKeyboard
import com.ivyclub.contact.util.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendFragment : BaseFragment<FragmentFriendBinding>(R.layout.fragment_friend) {

    private val viewModel: FriendViewModel by viewModels()
    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    viewModel.isSearchViewVisible.value == true -> {
                        viewModel.setSearchViewVisibility()
                    }
                    viewModel.isInLongClickedState.value == true -> {
                        friendListAdapter.setAllClickedClear(viewModel.longClickedId)
                        viewModel.clearLongClickedId()
                    }
                    else -> {
                        requireActivity().finish()
                    }
                }
            }
        }
    }
    private lateinit var friendListAdapter: FriendListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        initBackPressedCallback()
        initAddButton()
        initSettingsButton()
        initFriendListAdapter()
        observeSearchViewVisibility()
        observeFriendList()
        viewModel.getFriendData()
        getGroupSelectFragmentResult()
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
            if (friendListAdapter.isOneOfItemLongClicked()) {
                menuInflater.inflate(R.menu.menu_set_friends_at_friendlist, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.item_move_friends_to -> {
                            SelectGroupFragment().show(
                                childFragmentManager,
                                SelectGroupFragment.TAG
                            )
                        }
                    }
                    false
                }
            } else {
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
            }
            popupMenu.show()
        }
    }

    private fun initSettingsButton() = with(binding) {
        ivSettingsIcon.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_friend_to_settingsFragment)
        }
    }

    private fun showDialog() {
        AddGroupDialogFragment().show(childFragmentManager, ADD_GROUP_DIALOG_TAG)
    }

    private fun initFriendListAdapter() {
        friendListAdapter = FriendListAdapter(
            onGroupClick = viewModel::manageGroupFolded,
            onFriendClick = this::navigateToFriendDetailFragment,
            onFriendLongClick = viewModel::setLongClickedId
        )
        binding.rvFriendList.adapter = friendListAdapter
    }

    private fun observeSearchViewVisibility() {
        viewModel.isSearchViewVisible.observe(viewLifecycleOwner) { newVisibilityState ->
            with(binding) {
                if (newVisibilityState) {
                    showKeyboard()
                    etSearch.changeVisibilityWithDirection(
                        Gravity.TOP,
                        View.VISIBLE,
                        ANIMATION_TIME,
                        this@FriendFragment::requestFocus
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

    private fun requestFocus() {
        binding.etSearch.requestFocus()
    }

    private fun observeFriendList() {
        viewModel.friendList.observe(viewLifecycleOwner) { newFriendList ->
            // 새로운 리스트로 리사이클러뷰 갱신
            friendListAdapter.submitList(newFriendList)
        }
    }

    private fun navigateToFriendDetailFragment(friendId: Long) {
        findNavController().navigate(
            FriendFragmentDirections.actionNavigationFriendToFriendDetailFragment(
                friendId
            )
        )
    }

    private fun getGroupSelectFragmentResult() {
        childFragmentManager.setFragmentResultListener("requestKey", this) { key, bundle ->
            val result = bundle.getString("bundleKey")
            viewModel.updateFriendsGroup(result) // 뷰모델에서 클릭 된 아이템 처리 해제
            friendListAdapter.clearLongClickedItemCount() // 리스트 어댑터에서 클릭 된 아이템 처리 해제
            binding.rvFriendList.adapter = friendListAdapter
        }
    }

    companion object {
        private const val ANIMATION_TIME = 150L
        private const val ADD_GROUP_DIALOG_TAG = "AddGroupDialog"
    }
}