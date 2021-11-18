package com.ivyclub.contact.ui.main.plan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentPlanBinding
import com.ivyclub.contact.ui.plan_list.PlanListAdapter
import com.ivyclub.contact.ui.plan_list.PlanListHeaderItemDecoration
import com.ivyclub.contact.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlanFragment : BaseFragment<FragmentPlanBinding>(R.layout.fragment_plan) {

    private val viewModel: PlanViewModel by viewModels()

    private val planListAdapter: PlanListAdapter by lazy {
        PlanListAdapter {
            findNavController().navigate(
                PlanFragmentDirections.actionNavigationPlanToPlanDetailsFragment(it)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        initToolbarButtons()
        initRecyclerView()
        observePlanListItems()
    }

    private fun initToolbarButtons() {
        with(binding) {
            ivAddPlanIcon.setOnClickListener {
                findNavController().navigate(PlanFragmentDirections.actionNavigationPlanToAddEditFragment())
            }

            ivSettingsIcon.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_plan_to_settingsFragment)
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvPlanList.apply {
            if (adapter == null) {
                adapter = planListAdapter

                addItemDecoration(PlanListHeaderItemDecoration(object :
                    PlanListHeaderItemDecoration.SectionCallback {
                    override fun isHeader(position: Int) =
                        planListAdapter.isHeader(position)

                    override fun getHeaderView(list: RecyclerView, position: Int) =
                        planListAdapter.getHeaderView(list, position)
                }))
            }
        }
    }

    private fun observePlanListItems() {
        viewModel.planListItems.observe(viewLifecycleOwner) {
            planListAdapter.submitList(it)
        }
    }
}