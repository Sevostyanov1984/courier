package com.cdek.courier.ui.base.task.robocall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.cdek.courier.R
import com.cdek.courier.ui.base.task.TaskFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.bottomsheet_robocall.*


class RobocallBottomSheetDialog : BottomSheetDialogFragment() {

    companion object {
        var TAG = "RobocallBottomSheetDialog2"
        lateinit var listener: DialogTimeListener

        fun show(
            fragment: TaskFragment
        ) {
            RobocallBottomSheetDialog().apply {
                listener = fragment
            }.show(
                fragment.childFragmentManager,
                TAG
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_robocall, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewpager_robocall.adapter =
            RobocallBottomSheetDialogAdapter(this)
    }

    class RobocallBottomSheetDialogAdapter(val ctx: RobocallBottomSheetDialog) : PagerAdapter() {

        val layouts = arrayOf(
            Pair("Буду в течение часа", R.layout.robocall_buttons_views1),
            Pair("Я опоздаю", R.layout.robocall_buttons_views2),
            Pair("Запросить изменение времени приезда", R.layout.robocall_buttons_views3)
        )

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view: View =
                LayoutInflater.from(ctx.context).inflate(layouts[position].second, null, false);

            val btn: MaterialButton = view.findViewById<MaterialButton>(R.id.btn)
            btn.setOnClickListener(View.OnClickListener {
                /*when (position){
                     0 -> {
                         listener.pickRobocall(
                             RobocallTypes.NORMAL
                         )
                         ctx.dismiss()
                     }
                     1 -> {
                         PickLateTimeDialog.show(
                             ctx,
                             listener
                         )
                     }
                     2 -> {
                         ChangeTimeDialog.show(
                             ctx,
                             listener
                         )
                     }
                 }*/
            })

            container.addView(view)
            return view
        }

        override fun isViewFromObject(view: View, myObject: Any): Boolean {
            return view == myObject
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return layouts[position].first
        }

        override fun destroyItem(container: ViewGroup, position: Int, myObject: Any) {
            val view: View = myObject as View
            container.removeView(view)
        }
    }
}