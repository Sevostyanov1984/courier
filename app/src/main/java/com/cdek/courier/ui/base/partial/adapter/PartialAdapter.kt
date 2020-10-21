package com.cdek.courier.ui.base.partial.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cdek.courier.R
import com.cdek.courier.data.models.entities.task.Place
import com.cdek.courier.databinding.ItemPartialBinding

class PartialAdapter(
    private var items: List<Place>,
    private var listener: OnItemClickListener,
    private val canPartDelivery: Boolean
) : RecyclerView.Adapter<PartialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartialViewHolder {
        val binding: ItemPartialBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_partial,
                parent,
                false
            )
        return PartialViewHolder(binding, getMaxAmountChild())
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PartialViewHolder, position: Int) =
        holder.bind(items[position], position, listener, canPartDelivery)

    fun setData(list: List<Place>) {
        items = list
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(parent: Int, child: Int, operation: String)
    }

    private fun getMaxAmountChild(): Int {
        var intMaxNoOfChild = 0
        for (item in items) {

            item.goods ?: return intMaxNoOfChild

            val intMaxSizeTemp: Int = item.goods?.size!!
            if (intMaxSizeTemp > intMaxNoOfChild) {
                intMaxNoOfChild = intMaxSizeTemp
            }
        }
        return intMaxNoOfChild
    }
}