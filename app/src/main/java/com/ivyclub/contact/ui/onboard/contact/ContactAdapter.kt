package com.ivyclub.contact.ui.onboard.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.databinding.ItemContactBinding
import com.ivyclub.contact.model.PhoneContactData

class ContactAdapter(
    private val setCheckboxState: (Boolean) -> Unit
): ListAdapter<PhoneContactData,ContactAdapter.ViewHolder>(diffUtil) {

    val addSet = mutableSetOf<PhoneContactData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun selectAllItem() {
        addSet.clear()
        currentList.forEach { addSet.add(it) }
        notifyDataSetChanged()
    }

    fun removeAllItem() {
        addSet.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemContactBinding
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.clParent.setOnClickListener {
                binding.cbAdd.isChecked = !binding.cbAdd.isChecked
            }
            binding.cbAdd.setOnCheckedChangeListener { _, checked ->
                if(checked) {
                    addSet.add(getItem(adapterPosition))
                } else {
                    addSet.remove(getItem(adapterPosition))
                }
                setCheckboxState(addSet.size == currentList.size)
            }
        }

        fun bind(data: PhoneContactData) {
            binding.tvName.text = data.name
            binding.tvPhoneNum.text = data.phoneNumber
            binding.cbAdd.isChecked = addSet.contains(data)
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<PhoneContactData>() {
            override fun areItemsTheSame(oldItem: PhoneContactData, newItem: PhoneContactData) =
                oldItem.phoneNumber == newItem.phoneNumber

            override fun areContentsTheSame(oldItem: PhoneContactData, newItem: PhoneContactData) =
                oldItem == newItem
        }
    }
}