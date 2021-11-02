package com.ivyclub.contact.ui.main.friend

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.transition.*
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentFriendBinding
import com.ivyclub.contact.util.BaseFragment

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        initBackPressedCallback()
        initClearButton()
        initAddButton()
        initFriendListAdapter()
        observeSearchTextChange()
        observeSearchViewVisibility()
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
        val friendListAdapter = FriendListAdapter()
        friendListAdapter.setFriendList(
            listOf(
                FriendItemData("정우진", "구글 디자이너 / 25세 / 여행"),
                FriendItemData("장성희", "트위터 개발자 / 25세 / 등산"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
                FriendItemData("박태훈", "페이스북 디자이너 / 35세 / 골프"),
                FriendItemData("이원중", "넷플릭스 디자이너 / 45세 / 개발"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
                FriendItemData("홍길동", "아마존 개발자 / 15세 / 탁구"),
            )
        )
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
            if (newVisibilityState) {
                binding.etSearch.changeVisibilityWithDirection(Gravity.TOP, View.VISIBLE)
            } else {
                hideKeyboard()
                binding.etSearch.changeVisibilityWithDirection(Gravity.TOP, View.GONE)
                binding.etSearch.text.clear()
                binding.ivRemoveEt.visibility = View.GONE
            }
        }
    }

    private fun View.changeVisibilityWithDirection(direction: Int, visibility: Int) {
        val transition: Transition = TransitionSet().apply {
            addTransition(Fade())
            addTransition(Slide(direction))
            duration = ANIMATION_TIME
            addTarget(this@changeVisibilityWithDirection)
            addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition) {}
                override fun onTransitionEnd(transition: Transition) {
                    (this@changeVisibilityWithDirection).visibility = visibility
                }

                override fun onTransitionCancel(transition: Transition) {}
                override fun onTransitionPause(transition: Transition) {}
                override fun onTransitionResume(transition: Transition) {}
            })
        }
        TransitionManager.beginDelayedTransition(
            this.parent as ViewGroup,
            transition
        )
    }

    private fun hideKeyboard() {
        ViewCompat.getWindowInsetsController(binding.root)?.hide(WindowInsetsCompat.Type.ime())
    }

    companion object {
        private const val ANIMATION_TIME = 300L
    }
}