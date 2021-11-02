package com.ivyclub.contact.ui.main.friend

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
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
        registerBackPressedCallback()
        observeSearchTextChange()
        observeSearchViewVisibility()
        initClearButton()
        initAddButton()
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
    }

    private fun registerBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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
            if (!newVisibilityState) {
                hideKeyboard()
                binding.etSearch.text.clear()
                binding.ivRemoveEt.visibility = View.GONE
            }
        }
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

    private fun hideKeyboard() {
        ViewCompat.getWindowInsetsController(binding.root)?.hide(WindowInsetsCompat.Type.ime())
    }
}