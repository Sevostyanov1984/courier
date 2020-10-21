package com.cdek.courier.ui.base.task.problem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cdek.courier.R


class ProblemAdapter : RecyclerView.Adapter<ProblemAdapter.ViewHolder>() {

    private var list = mutableListOf<TestProblem>()

    fun setData(data: MutableList<TestProblem>) {
        this.list = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_cardview_problems_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list?.get(position)?.let { problem ->
            holder.bind(problem)
            holder.imageView.setOnClickListener(View.OnClickListener { view ->
                list.removeAt(position)
                notifyDataSetChanged()
            })
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<View>(R.id.iv_expandnote_task) as ImageView
        fun bind(problem: TestProblem) {
            (itemView.findViewById<View>(R.id.tv_contact) as TextView).setText(problem.contact)
            (itemView.findViewById<View>(R.id.tv_note) as TextView).setText(problem.note)
            (itemView.findViewById<View>(R.id.tv_time) as TextView).setText(problem.time)
        }
    }


    data class TestProblem(
        val contact: String,
        val note: String,
        val time: String
    )


}