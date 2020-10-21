package com.cdek.courier.ui.base.tasklist.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cdek.courier.R
import com.cdek.courier.data.models.entities.task.Task
import com.cdek.courier.databinding.TaskAdapterItemBinding
import java.util.*
import kotlin.collections.ArrayList
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.widget.ImageView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.cdek.courier.utils.enums.TaskStates
import com.cdek.courier.utils.enums.TaskTypes


class TaskAdapter(
    private var listener: OnItemClickListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(),
    ItemTouchHelperAdapter {

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val prev = list.removeAt(fromPosition)
        list.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, prev)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        list.removeAt(position);
        notifyItemRemoved(position);
    }

    var list: MutableList<Task> = ArrayList()
    var copyList: MutableList<Task> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        val binding: TaskAdapterItemBinding = DataBindingUtil
            .inflate(LayoutInflater.from(parent.context), R.layout.task_adapter_item, parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        /*if (list[position].urgency == true){
            holder.iconUrgent.visibility = View.VISIBLE
        }*/
        holder.bindItem(list[position], listener)
    }

    fun setData(_tasks: List<Task>) {
        list.clear()
        list.addAll(_tasks.toMutableList())
        copyList.clear()
        copyList.addAll(_tasks.toMutableList())
    }

    fun filterData(filteredText: String) {
        list.clear()
        if (filteredText.length == 0) {
            list.addAll(copyList)
        } else {
            copyList.forEach lit@{ task ->

                task.receiver?.let {
                    if (it.toLowerCase(Locale.getDefault()).contains(filteredText.toLowerCase(Locale.getDefault()))) {
                        list.add(task)
                        return@lit
                    }
                }

                task.basisNumber?.let {
                    if (it.toLowerCase(Locale.getDefault()).contains(filteredText.toLowerCase(Locale.getDefault()))) {
                        list.add(task)
                        return@lit
                    }
                }

                task.receiverAddress?.targetAddress?.let {
                    if (it.toLowerCase(Locale.getDefault()).contains(filteredText.toLowerCase(Locale.getDefault()))) {
                        list.add(task)
                        return@lit
                    }
                }

                task.receiverContactFace?.let {
                    if (it.toLowerCase(Locale.getDefault()).contains(filteredText.toLowerCase(Locale.getDefault()))) {
                        list.add(task)
                        return@lit
                    }
                }

                task.numberIm?.let {
                    if (it.toLowerCase(Locale.getDefault()).contains(filteredText.toLowerCase(Locale.getDefault()))) {
                        list.add(task)
                        return@lit
                    }
                }

                task.senderAddress?.targetAddress?.let {
                    if (it.toLowerCase(Locale.getDefault()).contains(filteredText.toLowerCase(Locale.getDefault()))) {
                        list.add(task)
                        return@lit
                    }
                }

                task.senderContactFace?.let {
                    if (it.toLowerCase(Locale.getDefault()).contains(filteredText.toLowerCase(Locale.getDefault()))) {
                        list.add(task)
                        return@lit
                    }
                }

                task.tariffName?.let {
                    if (it.toLowerCase(Locale.getDefault()).contains(filteredText.toLowerCase(Locale.getDefault()))) {
                        list.add(task)
                        return@lit
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(private val dataItemBinding: TaskAdapterItemBinding) :
        RecyclerView.ViewHolder(dataItemBinding.root) {

        val iconUrgent = dataItemBinding.iconUrgent

        fun bindItem(task: Task, listener: OnItemClickListener?) {
            dataItemBinding.task = task

            // отображение иконок таскитема
            if (task.urgency == true) {
                //dataItemBinding.iconUrgent.visibility = View.VISIBLE

                // анимация пульсирования
                val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                    dataItemBinding.iconUrgent,
                    PropertyValuesHolder.ofFloat("scaleX", 1.4f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.4f)
                )
                scaleDown.duration = 600
                scaleDown.repeatCount = ObjectAnimator.INFINITE
                scaleDown.repeatMode = ObjectAnimator.REVERSE
                scaleDown.setInterpolator(FastOutSlowInInterpolator())
                scaleDown.start()
            }

            // отображение обеденного времени
            if (task.lunchTimeFrom!!.equals("00:00") or task.lunchTimeTo!!.equals("00:00")) {

                dataItemBinding.tvLunchTaskAdaprterItem.visibility = View.GONE
                dataItemBinding.ivLunch.visibility = View.GONE
            } else {
                dataItemBinding.tvLunchTaskAdaprterItem.visibility = View.VISIBLE
                dataItemBinding.ivLunch.visibility = View.VISIBLE
            }


            // отображение основной иконки таскаитема
            when (task.taskType) {
                TaskTypes.DELIVERY.toString() -> {
                    dataItemBinding.iv.setImageResource(R.drawable.icon_account_right)
                    if (task.urgency == true) {
                        dataItemBinding.iv.setBackgroundResource(R.drawable.background_animation_list_urgent_delivery)
                        val anim: AnimationDrawable =
                            dataItemBinding.iv.getBackground() as AnimationDrawable
                        anim.setEnterFadeDuration(300)
                        anim.setExitFadeDuration(300)
                        anim.start()

                    } else {
                        dataItemBinding.iv.setBackgroundResource(R.drawable.background_shape_oval_delivery)
                    }
                }
                TaskTypes.DEMAND.toString() -> {

                    if (task.urgency == true) {
                        dataItemBinding.iv.setBackgroundResource(R.drawable.background_animation_list_urgent_order)
                        val anim: AnimationDrawable =
                            dataItemBinding.iv.getBackground() as AnimationDrawable
                        anim.setEnterFadeDuration(300)
                        anim.setExitFadeDuration(300)
                        anim.start()

                    } else {
                        dataItemBinding.iv.setBackgroundResource(R.drawable.background_shape_oval_order)
                    }
                }
            }

            // отображение состояния таска

            when (task.taskState) {
                TaskStates.ADDED.toString() -> {
                    //dataItemBinding.iv.setBackgroundResource(R.drawable.background_shape_oval_delivery)
                    //dataItemBinding.cardTask.alpha = 1f
                    dataItemBinding.tvStreetTaskAdaprterItem.setTextColor(Color.BLACK)
                    dataItemBinding.tvNameTaskAdaprterItem.setTextColor(Color.BLACK)
                    dataItemBinding.tvTypeTaskAdaprterItem.setTextColor(Color.BLACK)
                    dataItemBinding.tvBasenumberTaskAdaprterItem.setTextColor(Color.BLACK)
                    dataItemBinding.tvSumtopayTaskAdaprterItem.setTextColor(Color.BLACK)
                    dataItemBinding.tvLunchTaskAdaprterItem.setTextColor(Color.BLACK)
                    dataItemBinding.tvTimeTaskAdaprterItem.setTextColor(Color.BLACK)
                }
                TaskStates.RECEIVE.toString() -> {
                    //dataItemBinding.iv.setBackgroundResource(R.drawable.background_shape_oval_delivery)
                    //dataItemBinding.cardTask.alpha = 1f
                    dataItemBinding.tvStreetTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvNameTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvTypeTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvBasenumberTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvSumtopayTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvLunchTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvTimeTaskAdaprterItem.setTextColor(Color.GRAY)
                }
                TaskStates.COMPLETE.toString() -> {
                    dataItemBinding.iv.setBackgroundResource(R.drawable.background_shape_oval_disabled)
                    //dataItemBinding.cardTask.alpha = 1f
                    dataItemBinding.tvStreetTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvNameTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvTypeTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvBasenumberTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvSumtopayTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvLunchTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvTimeTaskAdaprterItem.setTextColor(Color.GRAY)
                }
                TaskStates.NOT_COMPLETE.toString() -> {
                    dataItemBinding.iv.setBackgroundResource(R.drawable.background_shape_oval_disabled)
                    //dataItemBinding.cardTask.alpha = 1f
                    dataItemBinding.tvStreetTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvNameTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvTypeTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvBasenumberTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvSumtopayTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvLunchTaskAdaprterItem.setTextColor(Color.GRAY)
                    dataItemBinding.tvTimeTaskAdaprterItem.setTextColor(Color.GRAY)
                }
            }

            listener?.let {
                dataItemBinding.root.setOnClickListener { listener.onItemClick(task) }
            }


            //dataItemBinding.executePendingBindings()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
//        fun onItemClick(item: Task)
    }
}