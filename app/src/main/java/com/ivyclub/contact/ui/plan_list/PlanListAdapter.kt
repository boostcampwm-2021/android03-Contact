package com.ivyclub.contact.ui.plan_list

import android.view.LayoutInflater
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

    fun isHeader(position: Int): Boolean {
        if (position == 0) return true

        val curCalendar = Calendar.getInstance()
        curCalendar.time = getItem(position).date
        val lastCalendar = Calendar.getInstance()
        lastCalendar.time = getItem(position - 1).date

        return curCalendar.get(Calendar.MONTH) != lastCalendar.get(Calendar.MONTH)
    }

    fun getHeaderView(rv: RecyclerView, position: Int): View? {
        val item = getItem(position)
        val date = item.date

        val binding = ItemPlanListHeaderBinding.inflate(LayoutInflater.from(rv.context), rv, false)
        binding.tvPlanMonth.text = "${date.getExactMonth()}월"
        binding.tvPlanYear.text = "${date.getExactYear()}"
        return binding.root
    }

    inner class PlanViewHolder(
        private val binding: ItemPlanListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: AppointmentData) {
            val context = itemView.context

            with(binding) {
                val date = data.date

                tvPlanMonth.text = "${date.getExactMonth()}월"
                tvPlanYear.text = date.getExactYear().toString()
                tvPlanDate.text = "${date.getDayOfMonth()}일 ${date.getDayOfWeek().korean}}"

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

                llMonthYear.visibility = if (isHeader(adapterPosition)) View.VISIBLE else View.INVISIBLE
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