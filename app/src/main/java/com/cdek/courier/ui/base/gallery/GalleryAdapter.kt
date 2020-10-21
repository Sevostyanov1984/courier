package com.cdek.courier.ui.base.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cdek.courier.R
import com.cdek.courier.data.models.entities.FileEntity
import com.cdek.courier.databinding.ItemGalleryBinding

class GalleryAdapter(
    private var items: List<FileEntity>,
    private var listener: OnItemClickListener
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemGalleryBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_gallery,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position], listener)

    inner class ViewHolder(private var binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FileEntity, listener: OnItemClickListener?) {
            binding.item = item
            if (listener != null) {
                binding.root.setOnClickListener { listener.onItemClick(item) }
            }

            binding.executePendingBindings()
        }
    }

    fun setData(list: List<FileEntity>) {
        items = list
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(item: FileEntity)
    }
}