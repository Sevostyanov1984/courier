package com.cdek.courier.ui.base.task.problem

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.cdek.courier.R
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ProblemDialog : DialogFragment() {

    interface ProblemDialogListener {
        fun sendProblem(value: Int)
    }

    companion object {
        var TAG = "ProblemDialog"
        val BUNDLE_VALUE = "bundle_value"
        lateinit var _problems: MutableList<String>

        fun show(
            fragment: Fragment,
            bundle_value: Int,
            problems: List<String>
        ) {
            ProblemDialog().apply {
                arguments = Bundle().apply {
                    putInt(BUNDLE_VALUE, bundle_value)
                }
                _problems = problems.toMutableList()
            }.show(
                fragment.childFragmentManager,
                TAG
            )
        }
    }

    private var listener: ProblemDialogListener? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProblemDialogListener) {
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val rootView = LayoutInflater.from(getContext())
            .inflate(R.layout.dialog_problem, null, false);

        val btn_cancel_problem = rootView.findViewById(R.id.btn_cancel_problem) as Button
        val btn_send_problem = rootView.findViewById(R.id.btn_send_problem) as Button
        val actv_problem = rootView.findViewById(R.id.actv_problem) as AutoCompleteTextView

        //val problems = mutableListOf("test1", "test2", "test3", "asd", "test12")

        val adapter = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_dropdown_item_1line,
            _problems
        )

        return AlertDialog.Builder(context!!)
            .setTitle("Создание проблемы")
            .setView(rootView)
            .create()
            .apply {
                setCanceledOnTouchOutside(false);
                setCancelable(false)
                btn_cancel_problem.setOnClickListener({
                    this.dismiss()
                })
                btn_send_problem.setOnClickListener({
                    listener?.sendProblem(1)
                })
                actv_problem.setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                        when (event?.action) {
                            MotionEvent.ACTION_UP ->
                                actv_problem.showDropDown()
                        }
                        return v?.onTouchEvent(event) ?: true
                    }
                })

                actv_problem.setAdapter(adapter)
            }

        RxTextView.textChangeEvents(actv_problem)
            //.debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { textViewTextChangeEvent -> textViewTextChangeEvent.text().toString() }
            //.filter { text -> text.length >= 3 }
            .subscribe { x ->
                {
                    _problems.addAll(_problems.filter { p -> p.startsWith(x) });
                    adapter.notifyDataSetChanged()
                }
            }
    }


}