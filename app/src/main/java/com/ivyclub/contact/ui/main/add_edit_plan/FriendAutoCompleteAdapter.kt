package com.ivyclub.contact.ui.main.add_edit_plan

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemFriendAutoCompleteBinding
import com.ivyclub.contact.util.binding

class FriendAutoCompleteAdapter(
    context: Context,
    private val friendList: List<Pair<String, String>>,
) : ArrayAdapter<Pair<String, String>>(context, 0, friendList.toMutableList()) {

    private lateinit var binding: ItemFriendAutoCompleteBinding

    private val friendFilter = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val results = FilterResults()
            val suggestions = mutableListOf<Pair<String, String>>()

            if (p0.isNullOrEmpty()) suggestions.addAll(friendList)
            else {
                val filterPattern = p0.toString().lowercase().trim()

                suggestions.addAll(
                    friendList.filter {
                        it.second.lowercase().trim().contains(filterPattern)
                    }
                )
            }

            return results.apply {
                values = suggestions
                count = suggestions.size
            }
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            clear()
            addAll(p1?.values as List<Pair<String, String>>)
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as Pair<String, String>).second
        }
    }

    override fun getFilter(): Filter {
        return friendFilter
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        binding = parent.binding(R.layout.item_friend_auto_complete)

        getItem(position)?.let { pair ->
            with(binding) {
                tvFriendPhone.text = pair.first
                tvFriendName.text = pair.second
            }
        }

        return binding.root
    }
}