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
import com.google.android.material.button.MaterialButton
import android.content.res.Resources
import android.widget.NumberPicker
import android.widget.TimePicker
import com.cdek.courier.ui.base.task.robocall2.RobocallBottomSheetDialog2
import kotlinx.android.synthetic.main.dialog_changetime.*
import java.util.*


class ChangeTimeDialog : DialogFragment() {

    private var dialogView: View? = null

    companion object {
        var TAG = "ChangeTimeDialog"
        lateinit var robocallBottomSheetDialog: RobocallBottomSheetDialog2
        lateinit var listener: DialogTimeListener

        fun show(
            _robocallBottomSheetDialog: RobocallBottomSheetDialog2,
            _listener: DialogTimeListener
        ) {
            ChangeTimeDialog().apply {
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
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        }

        return dialogView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_changetime, null, false)

        val tp_from: TimePicker = dialogView!!.findViewById(R.id.tp)
        val tp_to: TimePicker = dialogView!!.findViewById(R.id.tp2)
        val btn: MaterialButton = dialogView!!.findViewById(R.id.btn_submit_change_arrival)

        val minutes_from: NumberPicker? =
            tp_from.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"))
        val minutes_to: NumberPicker? =
            tp_to.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"))


        return AlertDialog.Builder(context!!, android.R.style.Theme_Dialog)
            .setView(dialogView)
            .create()
            .apply {
                setCanceledOnTouchOutside(true);
                setCancelable(true)
                tp_from.setIs24HourView(true)
                tp_to.setIs24HourView(true)
                minutes_from?.minValue = 0
                minutes_from?.maxValue = 1
                minutes_from?.displayedValues = arrayOf("00", "30")
                minutes_to?.minValue = 0
                minutes_to?.maxValue = 1
                minutes_to?.displayedValues = arrayOf("00", "30")

                // методы переопределяются чтобы при изменении минут не менялись часы
                minutes_from?.setOnValueChangedListener(NumberPicker.OnValueChangeListener { numberPicker, i, i2 ->
                })
                minutes_to?.setOnValueChangedListener(NumberPicker.OnValueChangeListener { numberPicker, i, i2 ->
                })

                btn.setOnClickListener({

                    val dateAndTime = Calendar.getInstance()

                    if (tp_to.currentHour < tp_from.currentHour) {
                        tv_change_time_error.visibility = View.VISIBLE
                        tv_change_time_error.text = "Конечное время меньше начального"
                    } else if ((tp_to.currentHour - tp_from.currentHour) < 3 ||
                        ((tp_to.currentHour - tp_from.currentHour) == 3) and (tp_from.currentMinute > tp_to.currentMinute)
                    ) {
                        tv_change_time_error.visibility = View.VISIBLE
                        tv_change_time_error.text = "Интервал менее 3 часов"
                    } else if ((dateAndTime.get(Calendar.HOUR_OF_DAY) > tp_to.currentHour) ||
                        ((dateAndTime.get(Calendar.HOUR_OF_DAY) == tp_to.currentHour) and (dateAndTime.get(
                            Calendar.MINUTE
                        ) > tp_to.currentMinute))
                    ) {
                        tv_change_time_error.visibility = View.VISIBLE
                        tv_change_time_error.text = "Текущий интервал приезда по Заказу закончен"
                    } else {
                        listener.pickRobocallChangeTime(
                            tp_from.currentHour,
                            tp_from.currentMinute * 30,
                            tp_to.currentHour,
                            tp_to.currentMinute * 30
                        )
                        dialog?.dismiss()
                        robocallBottomSheetDialog.dismiss()
                    }
                })
            }
    }
}