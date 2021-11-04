package com.ivyclub.contact.ui.main.plan

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentPlanBinding
import com.ivyclub.contact.ui.plan_list.PlanListAdapter
import com.ivyclub.contact.ui.plan_list.PlanListHeaderItemDecoration
import com.ivyclub.contact.util.BaseFragment

class PlanFragment : BaseFragment<FragmentPlanBinding>(R.layout.fragment_plan) {

    private val viewModel: PlanViewModel by viewModels()

    private val planListAdapter: PlanListAdapter by lazy {
        PlanListAdapter {
            // TODO: 약속 상세 화면 이동
            Toast.makeText(requireContext(), "plan id : $it", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        initAddButton()
        initRecyclerView()
        observePlanListItems()

        viewModel.getMyPlans()
    }

    private fun initAddButton() {
        binding.ivAddPlanIcon.setOnClickListener {
            // TODO: 약속 추가 화면 이동
            Toast.makeText(requireContext(), "add new plan", Toast.LENGTH_SHORT).show()
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

                    override fun getHeaderLayoutView(list: RecyclerView, position: Int) =
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