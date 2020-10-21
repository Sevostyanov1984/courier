package com.cdek.courier.ui.base.task

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdek.courier.App
import com.cdek.courier.R
import com.cdek.courier.databinding.FragmentCompleteBinding
import com.cdek.courier.databinding.FragmentTaskBinding
import com.cdek.courier.utils.enums.LoadingTypes.*
import com.cdek.courier.ui.base.task.mapDialog.MapDialog
import com.cdek.courier.ui.base.task.problem.ProblemAdapter
import com.cdek.courier.ui.base.task.problem.ProblemDialog
import com.cdek.courier.ui.base.task.robocall.DialogTimeListener
import com.cdek.courier.utils.enums.RobocallTypes
import com.cdek.courier.ui.base.task.robocall2.RobocallBottomSheetDialog2
import com.cdek.courier.ui.photo.sign.SignActivity
import com.cdek.courier.utils.REQUEST_TAKE_SIGN
import com.cdek.courier.utils.SEND_STRING_INTENT
import com.cdek.courier.utils.STATE_ERROR
import com.cdek.courier.utils.TASK_NUMBER
import com.cdek.courier.utils.enums.TaskStates
import com.google.android.material.appbar.AppBarLayout
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_task.*
import kotlinx.android.synthetic.main.task_cardview_addphoto.*
import kotlinx.android.synthetic.main.task_cardview_alternateive_receiver.*
import kotlinx.android.synthetic.main.task_cardview_cargoplace.*
import kotlinx.android.synthetic.main.task_cardview_info.*
import kotlinx.android.synthetic.main.task_cardview_payment.*
import kotlinx.android.synthetic.main.task_cardview_problems.*
import kotlinx.android.synthetic.main.task_cardview_receiver.*
import kotlinx.android.synthetic.main.task_cardview_receiver.ib_dial_task
import kotlinx.android.synthetic.main.task_cardview_receiver.ib_map_task
import kotlinx.android.synthetic.main.task_cardview_robocall.*
import javax.inject.Inject


class TaskFragment : DaggerFragment(), ProblemDialog.ProblemDialogListener,
    DialogTimeListener {

    override fun pickRobocall(type: RobocallTypes) {
        val bundle = Bundle()
        bundle.putSerializable("type", type)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_taskItem_to_standardRobocallFragment, bundle)
    }

    override fun pickRobocallChangeTime(
        hourStart: Int,
        minuteStart: Int,
        hourEnd: Int,
        minuteEnd: Int
    ) {
        val bundle = Bundle()
        bundle.putSerializable("type", RobocallTypes.CHANGE)
        bundle.putInt("hourStart", hourStart)
        bundle.putInt("minuteStart", minuteStart)
        bundle.putInt("hourEnd", hourEnd)
        bundle.putInt("minuteEnd", minuteEnd)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_taskItem_to_standardRobocallFragment, bundle)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var appBar: AppBarLayout
    var taskNumber: String? = ""

    lateinit var mainView: View

    private val viewModel by lazy {
        ViewModelProviders.of(this@TaskFragment, viewModelFactory)[TaskViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTaskBinding = DataBindingUtil
            .inflate(LayoutInflater.from(context), R.layout.fragment_task, container, false)
        binding.viewModel = viewModel

        taskNumber = arguments?.getString(TASK_NUMBER)

        mainView = binding.root
        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appBar = activity!!.findViewById<AppBarLayout>(R.id.appBar_activity_base)
        appBar.setExpanded(true, false)

        initObserve()
        initLoadingDialog()

        viewModel.fetchProblems()

        // здесь не нужен livedata<boolean> фрагмента, иначе будет ебля с переворот экрана
        // реализовано коробочным решением
        ib_add_problem.setOnClickListener(View.OnClickListener {
            ProblemDialog.show(this, 1, viewModel.problemList)
        })

        ib_add_robocall.setOnClickListener(View.OnClickListener {
            RobocallBottomSheetDialog2.show(this)
        })

        tv_note_task.setOnClickListener(View.OnClickListener {
            expandTextView()
        })

        iv_expandnote_task.setOnClickListener(View.OnClickListener {
            expandTextView()
        })

        ib_dial_task.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: 8 800 951 8541"))
            startActivity(intent)
        })

        ib_map_task.setOnClickListener(View.OnClickListener {
            //findNavController().navigate(R.id.yandexMap)
            MapDialog.show(
                this, viewModel.task?.get()?.cityReceiver,
                viewModel.task?.get()?.receiverAddress?.targetAddress,
                viewModel.task?.get()?.receiverCityCode,
                viewModel.task?.get()?.cityReceiver,
                viewModel.task?.get()?.receiverAddress?.latitude,
                viewModel.task?.get()?.receiverAddress?.longitude
            )
        })

        // region TestRobocall adapter
        val dataRobocall = mutableListOf(
            ProblemAdapter.TestProblem("Робозвонок 1", "Робозвонок 1", "10.10 11:23"),
            ProblemAdapter.TestProblem("Робозвонок 2", "Робозвонок 1", "10.10 12:10"),
            ProblemAdapter.TestProblem("Робозвонок 3", "Робозвонок 1", "10.10 12:10")
        )

        lv_robocall_list_task.layoutManager = LinearLayoutManager(activity)
        val robocallAdapter = ProblemAdapter()
        robocallAdapter.setData(dataRobocall)
        lv_robocall_list_task.setAdapter(robocallAdapter)
        lv_robocall_list_task.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            ), -1
        )

        iv_expandrobocall_task.setOnClickListener(View.OnClickListener {

            if (lv_robocall_list_task.isGone) {
                lv_robocall_list_task.alpha = 0f
                lv_robocall_list_task.visibility = View.VISIBLE
                iv_expandrobocall_task.animate().rotation(-180f).setDuration(300).start()
                val animator1: ObjectAnimator =
                    ObjectAnimator.ofFloat(lv_robocall_list_task, "alpha", 0f, 1f)
                animator1.doOnEnd {
                    val animator2: ObjectAnimator =
                        ObjectAnimator.ofInt(nsv, "scrollY", cv_robocall.top - 10)
                    animator2.duration = 200
                    animator2.start()
                }
                animator1.duration = 300
                animator1.start()

            } else if (lv_robocall_list_task.isVisible) {
                iv_expandrobocall_task.animate().rotation(0f).setDuration(300).start()
                val animator1: ObjectAnimator =
                    ObjectAnimator.ofFloat(lv_robocall_list_task, "alpha", 1f, 0f)
                animator1.doOnEnd {
                    lv_robocall_list_task.visibility = View.GONE
                }
                animator1.duration = 300
                animator1.start()
            }
        })
        // endregion

        // region TestProblem adapter
        val data = mutableListOf(
            ProblemAdapter.TestProblem("проблема 1", "какой-то комментарий", "10.10 11:23"),
            ProblemAdapter.TestProblem("проблема 2", "еще комментарий", "10.10 12:10"),
            ProblemAdapter.TestProblem("проблема 3", "еще комментарий", "10.10 12:10")
        )

        lv_problem_list.layoutManager = LinearLayoutManager(activity)
        val adapter = ProblemAdapter()
        adapter.setData(data)
        lv_problem_list.setAdapter(adapter)
        lv_problem_list.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            ), -1
        )

        iv_expandproblems_task.setOnClickListener(View.OnClickListener {

            if (lv_problem_list.isGone) {
                lv_problem_list.alpha = 0f
                lv_problem_list.visibility = View.VISIBLE
                iv_expandproblems_task.animate().rotation(-180f).setDuration(300).start()
                val animator1: ObjectAnimator =
                    ObjectAnimator.ofFloat(lv_problem_list, "alpha", 0f, 1f)
                animator1.doOnEnd {
                    val animator2: ObjectAnimator =
                        ObjectAnimator.ofInt(nsv, "scrollY", card_problem.top - 10)
                    animator2.duration = 200
                    animator2.start()
                }
                animator1.duration = 300
                animator1.start()

            } else if (lv_problem_list.isVisible) {
                iv_expandproblems_task.animate().rotation(0f).setDuration(300).start()
                val animator1: ObjectAnimator =
                    ObjectAnimator.ofFloat(lv_problem_list, "alpha", 1f, 0f)
                animator1.doOnEnd {
                    lv_problem_list.visibility = View.GONE
                }
                animator1.duration = 300
                animator1.start()
            }
        })
    }

    private fun initLoadingDialog() {
        viewModel.eventFired = false
        viewModel.isLoading.postValue(END)
        val builder = AlertDialog.Builder(this.context)
        val dialogView = layoutInflater.inflate(R.layout.dialog_progress, null)
        val message = dialogView.findViewById<TextView>(R.id.message)
        message.text = "Пожалуйста подождите..."
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialogLoading = builder.create()

        viewModel.isLoading.observe(this@TaskFragment, Observer { it ->

            when (it) {
                START -> {
                    dialogLoading?.show()
                }
                END -> {
                    if (viewModel.eventFired) {
                        dialogLoading?.dismiss()
                    }
                }
            }
        })
    }

    private fun setViewLayout(id: Int): Unit {

        val rootView: ViewGroup = view as ViewGroup
        val binding2: FragmentCompleteBinding = DataBindingUtil
            .inflate(LayoutInflater.from(context), id, rootView, false)
        binding2.viewModel = viewModel

        val btn_complete_task: Button = binding2.root.findViewById(R.id.btn_complete_task)
        btn_complete_task.setOnClickListener(View.OnClickListener {
            findNavController().navigateUp()
        })

        rootView.removeAllViews();
        rootView.addView(binding2.root);
    }

    private fun initObserve() {

        viewModel.getMyTask(taskNumber).observe(this@TaskFragment, Observer {

            val state = it.taskState
            when (state) {
                TaskStates.ADDED.toString() -> {
                    btn_change_status_task.text = "Я прочитал"
                    iv_receiver_icon_info.setBackgroundResource(R.drawable.background_shape_oval_disabled)
                    iv_receiver_icon_problems.setBackgroundResource(R.drawable.background_shape_oval_disabled)
                    iv_receiver_icon_receiver.setBackgroundResource(R.drawable.background_shape_oval_disabled)
                    iv_receiver_icon_cargoplace.setBackgroundResource(R.drawable.background_shape_oval_disabled)
                    iv_receiver_icon_robocall.setBackgroundResource(R.drawable.background_shape_oval_disabled)
                    iv_receiver_icon_payment.setBackgroundResource(R.drawable.background_shape_oval_disabled)
                    iv_receiver_icon_addphoto.setBackgroundResource(R.drawable.background_shape_oval_disabled)
                    iv_receiver_icon_alt_receiver.setBackgroundResource(R.drawable.background_shape_oval_disabled)
                }

                TaskStates.RECEIVE.toString() -> {
                    btn_change_status_task.text = "Я выполнил"
                    iv_receiver_icon_info.setBackgroundResource(R.drawable.background_shape_oval_delivery)
                    iv_receiver_icon_problems.setBackgroundResource(R.drawable.background_shape_oval_delivery)
                    iv_receiver_icon_receiver.setBackgroundResource(R.drawable.background_shape_oval_delivery)
                    iv_receiver_icon_cargoplace.setBackgroundResource(R.drawable.background_shape_oval_delivery)
                    iv_receiver_icon_robocall.setBackgroundResource(R.drawable.background_shape_oval_delivery)
                    iv_receiver_icon_payment.setBackgroundResource(R.drawable.background_shape_oval_delivery)
                    iv_receiver_icon_addphoto.setBackgroundResource(R.drawable.background_shape_oval_delivery)
                    iv_receiver_icon_alt_receiver.setBackgroundResource(R.drawable.background_shape_oval_delivery)
                }

                TaskStates.COMPLETE.toString() -> {
                    setViewLayout(R.layout.fragment_complete)
                }

                TaskStates.NOT_COMPLETE.toString() -> {
                    setViewLayout(R.layout.fragment_complete)
                }
            }

        })

        viewModel.navigateToSign.observe(this@TaskFragment, Observer {
            if (it) {
                viewModel.navigateToSignHandled()
                runSignActivity()
            }
        })

        viewModel.navigateToGallery.observe(this@TaskFragment, Observer {
            if (it) {
                viewModel.navigateToGalleryHandled()
                startGalleryFragment()
            }
        })

        viewModel.navigateToPartial.observe(this@TaskFragment, Observer {
            if (it) {
                viewModel.navigateToPartialHandled()
                startPartialFragment()
            }
        })

        viewModel.message.observe(this@TaskFragment, Observer {
            if (it.isNotEmpty()) {
                showMessage(it)
                viewModel.messageHandled()
            }
        })
    }

    // раскрытие комментария с анимацией
    private fun expandTextView() {
        val collapsedMaxLines = 10
        lateinit var animation: ObjectAnimator

        if (tv_note_task.maxLines == collapsedMaxLines) {
            animation = ObjectAnimator.ofInt(tv_note_task, "maxLines", 1)
            //iv_expandnote_task.setImageDrawable(resources.getDrawable(R.drawable.icon_chevron_down))
            iv_expandnote_task.animate().rotation(0f).setDuration(300).start()
            dots.visibility = View.VISIBLE

        } else {
            animation = ObjectAnimator.ofInt(tv_note_task, "maxLines", collapsedMaxLines)
            //iv_expandnote_task.setImageDrawable(resources.getDrawable(R.drawable.icon_chevron_up))
            iv_expandnote_task.animate().rotation(-180f).setDuration(300).start()
            dots.visibility = View.INVISIBLE
        }

        animation.duration = 200
        animation.start()
    }

    private fun showMessage(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }

    private fun runSignActivity() {
        val intent = Intent(App.instance, SignActivity::class.java)
        intent.putExtra(TASK_NUMBER, viewModel.task?.get()?.basisNumber)
        startActivityForResult(intent, REQUEST_TAKE_SIGN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                when (requestCode) {
                    REQUEST_TAKE_SIGN -> if (data.hasExtra(SEND_STRING_INTENT)) {
                        viewModel.sendingData()
                    } else if (data.hasExtra(STATE_ERROR)) {
                        data.getStringExtra(STATE_ERROR)?.let {
                            showMessage(it)
                        }
                    }
                }
            }
        }
    }

    private fun startGalleryFragment() {
        val bundle = bundleOf(TASK_NUMBER to viewModel.task?.get()?.basisNumber)
        findNavController().navigate(R.id.action_taskItem_to_galleryFragment, bundle)
    }

    private fun startPartialFragment() {
        val bundle = bundleOf(TASK_NUMBER to viewModel.task?.get()?.basisNumber)
        findNavController().navigate(R.id.action_taskItem_to_partialFragment, bundle)
    }

    override fun sendProblem(value: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }
}