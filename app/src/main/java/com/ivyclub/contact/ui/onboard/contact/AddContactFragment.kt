package com.ivyclub.contact.ui.onboard.contact

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentAddContactBinding
import com.ivyclub.contact.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactFragment : BaseFragment<FragmentAddContactBinding>(R.layout.fragment_add_contact){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }

    private fun initButtons() {
        binding.button.setOnClickListener {
            val ani1 = AnimationUtils.loadAnimation(requireContext(),R.anim.button_down)
            val ani2 = AnimationUtils.loadAnimation(requireContext(),R.anim.text_gone)
            binding.button.startAnimation(ani1)
            binding.textView.startAnimation(ani2)

        }
    }
}