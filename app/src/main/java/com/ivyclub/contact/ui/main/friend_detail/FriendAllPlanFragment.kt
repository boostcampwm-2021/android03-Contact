package com.ivyclub.contact.ui.main.friend_detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.FragmentFriendAllPlanBinding
import com.ivyclub.contact.ui.plan_list.PlanListAdapter
import com.ivyclub.contact.ui.plan_list.PlanListHeaderItemDecoration
import com.ivyclub.contact.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendAllPlanFragment :
    BaseFragment<FragmentFriendAllPlanBinding>(R.layout.fragment_friend_all_plan) {
    private val viewModel: FriendAllPlanViewModel by viewModels()
    private val args: FriendAllPlanFragmentArgs by navArgs()

    private val planListAdapter: PlanListAdapter by lazy {
        PlanListAdapter {
            findNavController().navigate(
                FriendAllPlanFragmentDirections.actionFriendAllPlanFragmentToPlanDetailsFragment(it)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        initToolbarButtons()
        initRecyclerView()
        observePlanListItems()

        args.friendId.let { viewModel.getMyPlans(it) }
    }

    private fun initToolbarButtons() {
        with(binding) {
            ivAddPlanIcon.setOnClickListener {
                findNavController().navigate(
                    FriendAllPlanFragmentDirections.actionFriendAllPlanFragmentToAddEditFragment(
                        friendId = args.friendId
                    )
                )
            }
            ivBackIcon.setOnClickListener {
                findNavController().popBackStack()
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