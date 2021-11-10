package com.ivyclub.contact.ui.main.add_edit_plan

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemFriendAutoCompleteBinding
import com.ivyclub.contact.util.binding
import com.ivyclub.data.model.SimpleFriendData

class FriendAutoCompleteAdapter(
    context: Context,
    private val friendList: List<SimpleFriendData>,
) : ArrayAdapter<SimpleFriendData>(context, 0, friendList.toMutableList()) {

    private lateinit var binding: ItemFriendAutoCompleteBinding

    private val friendFilter = object : Filter() {
        override fun performFiltering(inputText: CharSequence?): FilterResults {
            val results = FilterResults()
            val suggestions = mutableListOf<SimpleFriendData>()

            if (inputText.isNullOrEmpty()) suggestions.addAll(friendList)
            else {
                val filterPattern = inputText.toString().lowercase().replace(" ", "")

                suggestions.addAll(
                    friendList.filter {
                        val str = it.name.lowercase().replace(" ", "")
                        str.contains(filterPattern)
                    }
                )
            }

            return results.apply {
                values = suggestions
                count = suggestions.size
            }
        }

        override fun publishResults(inputText: CharSequence?, filterResults: FilterResults?) {
            clear()
            addAll(filterResults?.values as List<SimpleFriendData>)
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as SimpleFriendData).name
        }
    }

    override fun getFilter(): Filter {
        return friendFilter
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        binding = parent.binding(R.layout.item_friend_auto_complete)

        getItem(position)?.let { data ->
            binding.friendData = data
        }

        return binding.root
    }
}