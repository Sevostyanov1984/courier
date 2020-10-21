package com.cdek.courier.ui.base.task.robocall2

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import com.cdek.courier.R
import com.cdek.courier.ui.base.task.TaskFragment
import com.cdek.courier.ui.base.task.robocall.*
import com.cdek.courier.utils.enums.RobocallTypes

class RobocallBottomSheetDialog2 : DialogFragment() {

    companion object {
        var TAG = "RobocallBottomSheetDialog2"
        lateinit var listener: DialogTimeListener

        fun show(
            fragment: TaskFragment
        ) {
            RobocallBottomSheetDialog2().apply {
                listener = fragment
            }.show(
                fragment.childFragmentManager,
                TAG
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val rootView = LayoutInflater.from(getContext())
            .inflate(R.layout.bottomsheet_robocall2, null, false);

        val cv_robocall1 = rootView.findViewById(R.id.cv_robocall1) as CardView
        val cv_robocall2 = rootView.findViewById(R.id.cv_robocall2) as CardView
        val cv_robocall3 = rootView.findViewById(R.id.cv_robocall3) as CardView

        return AlertDialog.Builder(context!!)
            .setView(rootView)
            .create()
            .apply {
                setCanceledOnTouchOutside(false);
                setCancelable(false)
                cv_robocall1.setOnClickListener({
                    this.dismiss()
                    listener.pickRobocall(
                        RobocallTypes.NORMAL
                    )
                })
                cv_robocall2.setOnClickListener({
                    PickLateTimeDialog.show(
                        this@RobocallBottomSheetDialog2,
                        listener
                    )
                })
                cv_robocall3.setOnClickListener({
                    ChangeTimeDialog.show(
                        this@RobocallBottomSheetDialog2,
                        listener
                    )
                })
            }
    }
}