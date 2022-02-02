package com.ivyclub.contact.ui.main.plan_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivyclub.contact.R
import com.ivyclub.contact.databinding.ItemPlanPhotoBinding
import com.ivyclub.data.image.ImageType

class PhotoAdapter(
    private val photoList: List<String>,
    private val planId: Long,
    private val invokeMoveToImageDetailFragment: () -> Unit
) :
    RecyclerView.Adapter<PhotoAdapter.PagerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PagerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemPlanPhotoBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_plan_photo, parent, false)
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(photoList[position], planId)
    }

    override fun getItemCount(): Int = photoList.size

    inner class PagerViewHolder(private val binding: ItemPlanPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            moveToImageDetailFragment()
        }

        fun bind(imageName: String, planId: Long) {
            binding.imageString = "${ImageType.PLAN_IMAGE.filePath}$planId/$imageName"
        }

        private fun moveToImageDetailFragment() {
            binding.ivPhoto.setOnClickListener {
                invokeMoveToImageDetailFragment()
            }
        }
    }
}