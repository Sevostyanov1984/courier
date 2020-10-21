package com.cdek.courier.ui.base.task.robocall

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cdek.courier.R
import com.cdek.courier.ui.base.task.robocall2.RobocallBottomSheetDialog2
import com.cdek.courier.utils.enums.RobocallTypes
import com.google.android.material.button.MaterialButton

class PickLateTimeDialog : DialogFragment() {

    private var dialogView: View? = null

    companion object {
        var TAG = "PickLateTimeDialog"
        lateinit var robocallBottomSheetDialog: RobocallBottomSheetDialog2
        lateinit var listener: DialogTimeListener

        fun show(
            _robocallBottomSheetDialog: RobocallBottomSheetDialog2,
            _listener: DialogTimeListener
        ) {
            PickLateTimeDialog().apply {
                robocallBottomSheetDialog = _robocallBottomSheetDialog
                listener = _listener
            }.show(
                robocallBottomSheetDialog.childFragmentManager,
                TAG
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Set transparent background and no title
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return dialogView

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_picklatetime, null, false)

        val late_30m: MaterialButton = dialogView!!.findViewById(R.id.late_30m)
        val late_1h: MaterialButton = dialogView!!.findViewById(R.id.late_1h)
        val late_2h: MaterialButton = dialogView!!.findViewById(R.id.late_2h)
        val late_3h: MaterialButton = dialogView!!.findViewById(R.id.late_3h)

        return AlertDialog.Builder(context!!)
            .setView(dialogView)
            .create()
            .apply {
                setCanceledOnTouchOutside(true);
                setCancelable(true)
                late_30m.setOnClickListener({
                    listener.pickRobocall(RobocallTypes.MIN30)
                    dialog?.dismiss()
                    robocallBottomSheetDialog.dismiss()
                })
                late_1h.setOnClickListener({
                    listener.pickRobocall(RobocallTypes.HOUR1)
                    dialog?.dismiss()
                    robocallBottomSheetDialog.dismiss()
                })
                late_2h.setOnClickListener({
                    listener.pickRobocall(RobocallTypes.HOUR2)
                    dialog?.dismiss()
                    robocallBottomSheetDialog.dismiss()
                })
                late_3h.setOnClickListener({
                    listener.pickRobocall(RobocallTypes.HOUR3)
                    dialog?.dismiss()
                    robocallBottomSheetDialog.dismiss()
                })
            }
    }
}