package com.cdek.courier.ui.photo.sign

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.transition.TransitionManager
import android.util.DisplayMetrics
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.cdek.courier.R
import com.cdek.courier.databinding.ActivitySignBinding
import com.cdek.courier.utils.*
import com.cdek.courier.utils.FileUtils.initBitmapFromView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_sign.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SignActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this@SignActivity, viewModelFactory)[SignViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySignBinding = DataBindingUtil.setContentView<ActivitySignBinding>(
            this@SignActivity,
            R.layout.activity_sign
        ).apply { lifecycleOwner = this@SignActivity }
        binding.viewModel = viewModel

        initPaintView()

        initObserve()

//        get task number
        intent.getStringExtra(TASK_NUMBER)?.let {
            viewModel.fetchTask(it)
        }

    }

    //    set display param for viewPaint
    private fun initPaintView() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        viewPaint.init(metrics)
    }

    private fun clearCanvas() {
        viewPaint.clear()
    }

    private fun changeStateView() {
        viewPaint.visibility = View.GONE
        fab_reset.visibility = View.GONE
        fl_signature.visibility = View.VISIBLE
        ll_order_info.visibility = View.VISIBLE
        ll_delivery_state.visibility = View.VISIBLE
        tv_agree.visibility = View.VISIBLE

        val set = ConstraintSet()
        // copy constraints settings from current ConstraintLayout to set
        set.clone(root_layout)
        // change constraints settings
        set.connect(R.id.fl_signature, ConstraintSet.START, R.id.ll_order_info, ConstraintSet.END)
        set.connect(R.id.fl_signature, ConstraintSet.BOTTOM, R.id.tv_agree, ConstraintSet.TOP)
        set.connect(
            R.id.fl_signature, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP
        )
        set.connect(
            R.id.fl_signature, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END
        )
        set.constrainWidth(R.id.fl_signature, ConstraintSet.MATCH_CONSTRAINT)
        set.constrainHeight(R.id.fl_signature, ConstraintSet.MATCH_CONSTRAINT)

        // enable animation
        TransitionManager.beginDelayedTransition(root_layout)
        // apply constraints settings from set to current ConstraintLayout
        set.applyTo(root_layout)
    }

    private fun initScreenShot() {
        val viewRoot: View = root_layout
        val imagePath =
            (Objects.requireNonNull(getExternalFilesDir(Environment.DIRECTORY_PICTURES))?.absolutePath.toString() + "/" + SimpleDateFormat(
                CREATE_DATE_TEMPLATE_SHORT,
                Locale.getDefault()
            ).format(Date()) + ".jpeg")
        val bmpScreen: Bitmap = initBitmapFromView(viewRoot)
        val error = FileUtils.saveBitmapToFile(bmpScreen, imagePath)
        if (error.isEmpty()) {
            returnToTaskFragment(true, imagePath)
        } else {
            returnToTaskFragment(false, error)
        }
    }

    private fun returnToTaskFragment(isSaved: Boolean, result: String) {
        val returnIntent = Intent()
        if (isSaved) {
            returnIntent.putExtra(SEND_STRING_INTENT, result)
        } else {
            returnIntent.putExtra(STATE_ERROR, result)
        }
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    private fun initObserve() {
        viewModel.clearCanvas.observe(this@SignActivity, Observer {
            if (it) {
                viewModel.clearCanvasHandled()
                clearCanvas()
            }
        })

        viewModel.acceptSign.observe(this@SignActivity, Observer {
            if (it) {
                if (viewModel.isPainting.get()) {
                    viewModel.isPainting.set(false)
                    viewModel.acceptSignHandled()
                    val signature: Bitmap = initBitmapFromView(viewPaint)
                    iv_signature.background = BitmapDrawable(resources, signature)
                    changeStateView()
                } else {
                    initScreenShot()
                }
            }
        })
    }

}
