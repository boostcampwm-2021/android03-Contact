package com.ivyclub.contact.ui.plan_list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemPlanListBinding
import com.ivyclub.contact.databinding.ItemPlanListHeaderBinding
import com.ivyclub.contact.util.*
import com.ivyclub.data.model.AppointmentData
import kotlin.math.abs

class PlanListAdapter(
    val onItemClick: (Long) -> (Unit)
) : ListAdapter<AppointmentData, PlanListAdapter.PlanViewHolder>(diffUtil) {

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
            currentList.map { it.date.time / DAY_IN_MILLIS }
                .forEachIndexed { index, day ->
                    val gap = abs(currentDay - day)
                    if (gap < minGap ||
                        (gap == minGap && currentDay < day)) {
                        minGap = gap
                        minIdx = index
                    }
                }

            recyclerView.scrollToPosition(minIdx)
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<AppointmentData>,
        currentList: MutableList<AppointmentData>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        scrollToRecentDateCallback.invoke()
    }

    fun isHeader(position: Int): Boolean {
        if (position == 0) return true

        val currentDate = getItem(position).date
        val lastDate = getItem(position - 1).date

        return currentDate.getExactMonth() != lastDate.getExactMonth()
    }

    fun getHeaderView(rv: RecyclerView, position: Int): View? {
        val item = getItem(position)
        val date = item.date

        val binding = rv.binding<ItemPlanListHeaderBinding>(R.layout.item_plan_list_header)
        binding.tvPlanMonth.text = "${date.getExactMonth()}월"
        binding.tvPlanYear.text = "${date.getExactYear()}"
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

        fun bind(data: AppointmentData) {
            val context = itemView.context
            planId = data.id

            with(binding) {
                val date = data.date

                tvPlanMonth.text = "${date.getExactMonth()}월"
                tvPlanYear.text = date.getExactYear().toString()
                tvPlanDate.text = "${date.getDayOfMonth()}일 ${date.getDayOfWeek().korean}"

                tvPlanTitle.text = data.title

                cgPlanFriends.removeAllViews()
                data.participant.subList(0, 3.coerceAtMost(data.participant.size)).forEachIndexed { index, name ->
                    Chip(context).apply {
                        text =
                            if (data.participant.size > 3 && index == 2) "$name 외 ${data.participant.size - 3}명"
                            else name
                        isEnabled = false
                        setChipBackgroundColorResource(R.color.blue_100)
                        setEnsureMinTouchTargetSize(false)
                        chipMinHeight = 8f
                    }.also {
                        cgPlanFriends.addView(it)
                    }
                }

                llMonthYear.visibility =
                    if (isHeader(adapterPosition)) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<AppointmentData>() {
            override fun areItemsTheSame(oldItem: AppointmentData, newItem: AppointmentData) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AppointmentData, newItem: AppointmentData) =
                oldItem == newItem
        }
    }
}