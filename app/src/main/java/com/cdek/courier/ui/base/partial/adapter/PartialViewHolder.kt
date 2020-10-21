package com.cdek.courier.ui.base.partial.adapter

import android.os.Build
import android.os.Handler
import android.text.Html
import android.view.Gravity
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cdek.courier.R
import com.cdek.courier.data.models.entities.task.Good
import com.cdek.courier.data.models.entities.task.Place
import com.cdek.courier.databinding.ItemPartialBinding
import com.cdek.courier.utils.OPERATION_DECREMENT
import com.cdek.courier.utils.OPERATION_INCREASE
import com.cdek.courier.utils.OPERATION_INCREMENT
import com.cdek.courier.utils.OPERATION_REDUCE

class PartialViewHolder(
    private var binding: ItemPartialBinding, maxAmountChild: Int
) : RecyclerView.ViewHolder(binding.root) {

    private val tvParentName: TextView
    private val llChildItems: LinearLayout
    private val ivArrow: ImageView

    init {
        val context = itemView.context

        tvParentName = binding.tvParentName
        llChildItems = binding.llChildItems
        val llParentItems = itemView.findViewById<LinearLayout>(R.id.ll_parent_items)
        ivArrow = binding.ivArrow
        ivArrow.setBackgroundResource(R.drawable.ic_expand_more)
        //      block Layout Params
        val layoutParamsForInclude = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val layoutParamsForName = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val layoutParamsForSummaAndCounter = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        )
        layoutParamsForSummaAndCounter.setMargins(16, 16, 16, 16)
        val layoutParamsForSumma = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParamsForSumma.weight = 10f
        layoutParamsForSumma.setMargins(20, 0, 0, 0)
        val layoutParamsForAmount = LinearLayout.LayoutParams(
            25, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParamsForAmount.weight = 2f
        val layoutParamsForBtn = LinearLayout.LayoutParams(
            30, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParamsForBtn.weight = 3f
        //      create child view
        for (indexView in 0 until maxAmountChild) { //        create LinearLayout for child
            val child = indexView
            val llInclude = LinearLayout(context)
            llInclude.layoutParams = layoutParamsForInclude
            llInclude.orientation = LinearLayout.VERTICAL
            llInclude.minimumHeight = 150
            llInclude.background = context.getDrawable(R.drawable.background_sub_module_text)
            //        fill llInclude
            val tvNameInclude = TextView(context)
            tvNameInclude.setPadding(16, 8, 8, 8)
            tvNameInclude.gravity = Gravity.CENTER or Gravity.START
            tvNameInclude.textSize = 14f
            //        create LinearLayout for Summa and Counter
            val llSummaAndCounter = LinearLayout(context)
            llSummaAndCounter.layoutParams = layoutParamsForSummaAndCounter
            llSummaAndCounter.orientation = LinearLayout.HORIZONTAL
            llSummaAndCounter.gravity = Gravity.CENTER
            val tvSummaInclude = TextView(context)
            tvSummaInclude.textSize = 16f
            tvSummaInclude.gravity = Gravity.CENTER or Gravity.START
            //        fill llCounterInclude
            val btnCounterMinus = ImageView(context)
            btnCounterMinus.setImageResource(R.drawable.ic_counter_minus)
            btnCounterMinus.isClickable
            val tvAmountInclude = TextView(context)
            tvAmountInclude.gravity = Gravity.CENTER
            val btnCounterPlus = ImageView(context)
            btnCounterPlus.setImageResource(R.drawable.ic_counter_plus)
            btnCounterMinus.isClickable
            llSummaAndCounter.addView(tvSummaInclude, layoutParamsForSumma)
            llSummaAndCounter.addView(btnCounterMinus, layoutParamsForBtn)
            llSummaAndCounter.addView(tvAmountInclude, layoutParamsForAmount)
            llSummaAndCounter.addView(btnCounterPlus, layoutParamsForBtn)
            llInclude.addView(tvNameInclude, layoutParamsForName)
            llInclude.addView(llSummaAndCounter)
            llChildItems.addView(llInclude)
        }
        llParentItems.setOnClickListener {
            if (llChildItems.visibility == View.VISIBLE) {
                llChildItems.visibility = View.GONE
                ivArrow.setBackgroundResource(R.drawable.ic_expand_more)
            } else {
                llChildItems.visibility = View.VISIBLE
                ivArrow.setBackgroundResource(R.drawable.ic_expand_less)
            }
        }
    }


    fun bind(
        item: Place,
        position: Int,
        listener: PartialAdapter.OnItemClickListener?,
        canPartDelivery: Boolean
    ) {
        binding.item = item
        binding.executePendingBindings()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemView.transitionName = "card_transition_$adapterPosition"
        }
        tvParentName.text = item.description
        val noOfChildViews: Int = llChildItems.childCount
        for (index in 0 until noOfChildViews) {
            val llInclude = llChildItems.getChildAt(index) as LinearLayout
            llInclude.visibility = View.VISIBLE
        }
        val noOfChild: Int = item.goods!!.size
        if (noOfChild < noOfChildViews) {
            for (index in noOfChild until noOfChildViews) {
                val llInclude = llChildItems.getChildAt(index) as LinearLayout
                llInclude.visibility = View.GONE
            }
        }
        for (childViewIndex in 0 until noOfChild) {
            val amount: Int = item.goods!![childViewIndex].countDelivered!!
            val llInclude = llChildItems
                .getChildAt(childViewIndex) as LinearLayout
            val llSummaAndCounter = llInclude.getChildAt(1) as LinearLayout
            val tvNameParcel = llInclude.getChildAt(0) as TextView
            val nameParcel: String = createOrderParcelText(item.goods!![childViewIndex])
            tvNameParcel.text = Html.fromHtml(nameParcel)
            val tvSummaInclude = llSummaAndCounter.getChildAt(0) as TextView
            val summaInclude: String =
                item.goods!![childViewIndex].price.toString() + "р"
            tvSummaInclude.text = summaInclude
            val tvAmountInclude = llSummaAndCounter.getChildAt(2) as TextView
            tvAmountInclude.text = amount.toString()
            val btnCounterMinus = llSummaAndCounter.getChildAt(1) as ImageView
            val btnCounterPlus = llSummaAndCounter.getChildAt(3) as ImageView
            if (canPartDelivery) {
                btnCounterMinus.setOnClickListener {
                    listener!!.onItemClick(position, childViewIndex, OPERATION_DECREMENT)
                }
                btnCounterMinus.setOnLongClickListener(object : OnLongClickListener {
                    private val mHandler = Handler()
                    private val runnable: Runnable = object : Runnable {
                        override fun run() {
                            mHandler.removeCallbacks(this)
                            if (btnCounterMinus.isPressed) {
                                listener!!.onItemClick(position, childViewIndex, OPERATION_REDUCE)
                                mHandler.postDelayed(this, 100)
                            }
                        }
                    }

                    override fun onLongClick(view: View): Boolean {
                        mHandler.postDelayed(runnable, 0)
                        return true
                    }
                })
                btnCounterPlus.setOnClickListener {
                    listener!!.onItemClick(position, childViewIndex, OPERATION_INCREMENT)
                }
                btnCounterPlus.setOnLongClickListener(object : OnLongClickListener {
                    private val mHandler = Handler()
                    private val runnable: Runnable = object : Runnable {
                        override fun run() {
                            mHandler.removeCallbacks(this)
                            if (btnCounterPlus.isPressed) {
                                listener!!.onItemClick(position, childViewIndex, OPERATION_INCREASE)
                                mHandler.postDelayed(this, 100)
                            }
                        }
                    }

                    override fun onLongClick(view: View): Boolean {
                        mHandler.postDelayed(runnable, 0)
                        return true
                    }
                })
            } else {
                btnCounterMinus.visibility = View.GONE
                btnCounterPlus.visibility = View.GONE
            }
        }
    }

    private fun createOrderParcelText(parcel: Good): String {
        val sb = StringBuilder()
        val nameTmp: String =
            if (parcel.name!!.length > 200) parcel.name!!.substring(200) else parcel.name!!
        if (parcel.markCode != null) {
            sb.append("<b>").append(nameTmp).append("</b>, ")
                .append("маркировка ").append(parcel.markCode).append(", ")
                .append("артикул ").append(parcel.vendorCode)
        } else {
            sb.append("<b>").append(nameTmp).append("</b>, ")
                .append("артикул ").append(parcel.vendorCode)
        }
        return sb.toString()
    }
}