package com.ivyclub.contact.ui.main.add_friend

import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemAddFriendExtraInfoBinding
import com.ivyclub.contact.util.binding

class ExtraInfoListAdapter :
    ListAdapter<FriendExtraInfoData, ExtraInfoListAdapter.ExtraInfoViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtraInfoViewHolder {
        return ExtraInfoViewHolder(parent.binding(R.layout.item_add_friend_extra_info))
    }

    override fun onBindViewHolder(holder: ExtraInfoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExtraInfoViewHolder(private val binding: ItemAddFriendExtraInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.etExtraInfoTitle.doOnTextChanged { text, _, _, _ ->
                val currentExtraInfo = getItem(adapterPosition)
                currentExtraInfo.title = text.toString()
            }
            binding.etExtraInfoValue.doOnTextChanged { text, _, _, _ ->
                val currentExtraInfo = getItem(adapterPosition)
                currentExtraInfo.value = text.toString()
            }
        }

        fun bind(extraInfo: FriendExtraInfoData) {
            with(binding) {
                etExtraInfoTitle.setText(extraInfo.title)
                etExtraInfoValue.setText(extraInfo.value)
            }
        }

    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<FriendExtraInfoData>() {
            override fun areItemsTheSame(
                oldItem: FriendExtraInfoData,
                newItem: FriendExtraInfoData
            ): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(
                oldItem: FriendExtraInfoData,
                newItem: FriendExtraInfoData
            ): Boolean =
                oldItem == newItem
        }
    }
}