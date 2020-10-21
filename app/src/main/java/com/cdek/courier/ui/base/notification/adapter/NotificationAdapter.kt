package com.cdek.courier.ui.base.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cdek.courier.R
import com.cdek.courier.data.models.entities.notification.Notification
import com.cdek.courier.databinding.NotificationAdapterItemBinding


class NotificationAdapter(
    private var items: List<Notification>,
    private var listener: OnItemClickListener
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: NotificationAdapterItemBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context),
                R.layout.notification_adapter_item,
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position], listener)

    inner class ViewHolder(private var binding: NotificationAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Notification, listener: OnItemClickListener?) {
            binding.item = item
            listener?.let {
                binding.root.setOnClickListener { listener.onItemClick(item) }
            }
            binding.executePendingBindings()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Notification)
    }

    fun setData(newData: List<Notification>) {
        items = newData
        notifyDataSetChanged()
    }
}