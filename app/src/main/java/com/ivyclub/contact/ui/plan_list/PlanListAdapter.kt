package com.ivyclub.contact.ui.plan_list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemPlanListBinding
import com.ivyclub.contact.databinding.ItemPlanListHeaderBinding
import com.ivyclub.contact.util.DAY_IN_MILLIS
import com.ivyclub.contact.util.binding
import com.ivyclub.contact.util.setFriendChips
import kotlin.math.abs

class PlanListAdapter(
    val onItemClick: (Long) -> (Unit)
) : ListAdapter<PlanListItemViewModel, PlanListAdapter.PlanViewHolder>(diffUtil) {

    private lateinit var scrollToRecentDateCallback: () -> (Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlanViewHolder(
            parent.binding(R.layout.item_plan_list)
        )

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        scrollToRecentDateCallback = {
            val currentDay = System.currentTimeMillis() / DAY_IN_MILLIS
            var minGap = currentDay
            var minIdx = 0
            currentList.map { it.dayCount }
                .forEachIndexed { index, day ->
                    val gap = abs(currentDay - day)
                    if (gap < minGap ||
                        (gap == minGap && currentDay < day)
                    ) {
                        minGap = gap
                        minIdx = index
                    }
                }

            recyclerView.scrollToPosition(minIdx)
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<PlanListItemViewModel>,
        currentList: MutableList<PlanListItemViewModel>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        if (previousList.isNotEmpty() && currentList.isNotEmpty() &&
                previousList[0].id != currentList[0].id &&
                previousList[0].planYear == currentList[0].planYear &&
                previousList[0].planMonth == currentList[0].planMonth) {
            notifyItemRangeChanged(0, 2)
        }
        scrollToRecentDateCallback.invoke()
    }

    fun isHeader(position: Int): Boolean {
        if (position == 0)
            return true

        val currentItem = getItem(position)
        val lastItem = getItem(position - 1)

        return currentItem.planMonth != lastItem.planMonth || currentItem.planYear != lastItem.planYear
    }

    fun getHeaderView(rv: RecyclerView, position: Int): View? {
        val binding = rv.binding<ItemPlanListHeaderBinding>(R.layout.item_plan_list_header)
        binding.viewModel = getItem(position)
        binding.executePendingBindings()
        return binding.root
    }

    inner class PlanViewHolder(
        private val binding: ItemPlanListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var planId: Long? = null

        init {
            itemView.setOnClickListener {
                planId?.let { id ->
                    onItemClick(id)
                }
            }
        }

        fun bind(itemViewModel: PlanListItemViewModel) {
            planId = itemViewModel.id

            with(binding) {
                viewModel = itemViewModel
                cgPlanFriends.setFriendChips(itemViewModel.friends, 3)
                llMonthYear.visibility =
                    if (isHeader(adapterPosition)) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<PlanListItemViewModel>() {
            override fun areItemsTheSame(
                oldItem: PlanListItemViewModel,
                newItem: PlanListItemViewModel
            ) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: PlanListItemViewModel,
                newItem: PlanListItemViewModel
            ) =
                oldItem == newItem
        }
    }
}