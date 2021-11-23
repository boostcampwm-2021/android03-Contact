package com.ivyclub.contact.ui.onboard.app_intro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemIntroBinding
import com.ivyclub.contact.util.setCustomBackgroundDrawable
import kotlin.reflect.KFunction0

class ViewPagerAdapter(
    private val introList: List<Int>,
    private val introStringList: List<String>,
    private val onClicked: KFunction0<Unit>
) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PagerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemIntroBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_intro, parent, false)
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(introList[position], introStringList[position])
        holder.setButton(position == 3)
    }

    override fun getItemCount(): Int = introList.size

    inner class PagerViewHolder(private val binding: ItemIntroBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnStart.setOnClickListener {
                onClicked()
            }
        }

        fun bind(image: Int, str: String) {
            binding.ivIntro.setCustomBackgroundDrawable(image)
            binding.tvIntro.text = str
        }

        fun setButton(visible: Boolean){
            binding.btnStart.isVisible = visible
        }
    }

}