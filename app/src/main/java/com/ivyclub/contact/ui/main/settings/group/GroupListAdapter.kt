package com.ivyclub.contact.ui.main.settings.group

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemGroupListBinding
import com.ivyclub.contact.util.binding
import com.ivyclub.data.model.GroupData

class GroupListAdapter(
    private val onDeleteButtonClick: (GroupData) -> Unit
) : ListAdapter<GroupData, GroupListAdapter.GroupViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(parent.binding(R.layout.item_group_list))
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GroupViewHolder(private val binding: ItemGroupListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivDeleteGroup.setOnClickListener {
                onDeleteButtonClick(getItem(adapterPosition))
            }
        }

        fun bind(groupData: GroupData) {
            binding.groupData = groupData
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<GroupData>() {
            override fun areItemsTheSame(oldItem: GroupData, newItem: GroupData): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: GroupData, newItem: GroupData): Boolean =
                oldItem == newItem
        }
    }
}