package com.ivyclub.contact.ui.plan_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemPlanListBinding
import com.ivyclub.contact.util.DayOfWeek
import com.ivyclub.data.model.AppointmentData
import java.util.*

class PlanListAdapter : ListAdapter<AppointmentData, PlanListAdapter.PlanViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlanViewHolder(
            ItemPlanListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlanViewHolder(
        private val binding: ItemPlanListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: AppointmentData) {
            val context = itemView.context
            with(binding) {

                val calendar = Calendar.getInstance()
                calendar.time = data.date
                val dayOfMonth = calendar.get(Calendar.DATE)
                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1

                tvPlanMonth.text = "${month}월"
                tvPlanYear.text = year.toString()
                tvPlanDate.text = "${dayOfMonth}일 ${DayOfWeek.values()[dayOfWeek - 1].korean}"

                tvPlanTitle.text = data.title

                cgPlanFriends.removeAllViews()
                data.participant.subList(0, 3.coerceAtMost(data.participant.size)).forEach {
                    Chip(context).apply {
                        text =
                            if (data.participant.size > 3) "$it 외 ${data.participant.size - 3}명"
                            else it
                        isEnabled = false
                        setChipBackgroundColorResource(R.color.purple_200)
                    }.also {
                        cgPlanFriends.addView(it)
                    }
                }
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