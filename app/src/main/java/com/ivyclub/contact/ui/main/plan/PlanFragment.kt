package com.ivyclub.contact.ui.main.plan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentPlanBinding
import com.ivyclub.contact.util.BaseFragment

class PlanFragment : BaseFragment<FragmentPlanBinding>(R.layout.fragment_plan) {

    private val viewModel: PlanViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
    }
}