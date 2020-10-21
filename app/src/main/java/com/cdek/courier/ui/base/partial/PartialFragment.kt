package com.cdek.courier.ui.base.partial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cdek.courier.R
import com.cdek.courier.data.models.entities.task.Place
import com.cdek.courier.databinding.FragmentPartialBinding
import com.cdek.courier.ui.base.partial.adapter.PartialAdapter
import com.cdek.courier.utils.TASK_NUMBER
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_partial.*
import javax.inject.Inject


class PartialFragment : DaggerFragment(), PartialAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var rvAdapter: PartialAdapter

    private val viewModel by lazy {
        ViewModelProviders.of(this@PartialFragment, viewModelFactory)[PartialViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPartialBinding = DataBindingUtil
            .inflate(LayoutInflater.from(context), R.layout.fragment_partial, container, false)
        binding.viewModel = viewModel

//        todo add DeliveryDetailResponseDto
        arguments?.getString(TASK_NUMBER)?.let {
            viewModel.fetchTask(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        initObserve()
    }


    private fun initRecyclerView() {
        rv_parcel.layoutManager = LinearLayoutManager(activity)
        rv_parcel.setHasFixedSize(true)
        //    add hide\show button
//        rv_parcel.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (dy > 0 && fab_calculate.visibility === View.VISIBLE) {
//                    fab_calculate.hide()
//                } else if (dy < 0 && fab_calculate.visibility !== View.VISIBLE) {
//                    fab_calculate.show()
//                }
//            }
//        })
    }

    override fun onItemClick(parent: Int, child: Int, operation: String) {
        //        todo add listener
        viewModel.updateData(parent, child, operation)
    }

    private fun initObserve() {
        viewModel.getPlace().observe(this@PartialFragment, Observer<List<Place>> {
            it?.let {
                if (::rvAdapter.isInitialized) {
                    rvAdapter.setData(it)
                } else {
                    rvAdapter =
                        PartialAdapter(
                            it,
                            this@PartialFragment,
                            viewModel.task.get()!!.canPartDelivery!!
                        )
                    rv_parcel.adapter = rvAdapter
                }
            }
        })

        viewModel.navigateToTask.observe(this@PartialFragment, Observer {
            if (it) {
                viewModel.navigateToTaskHandled()
                findNavController().navigateUp()
            }
        })

        viewModel.message.observe(this@PartialFragment, Observer {
            if (it.isNotEmpty()) {
                showMessage(it)
                viewModel.messageHandled()
            }
        })

    }

    private fun showMessage(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }
}