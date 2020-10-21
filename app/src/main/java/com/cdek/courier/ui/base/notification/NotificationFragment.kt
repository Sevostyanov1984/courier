package com.cdek.courier.ui.base.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdek.courier.R
import com.cdek.courier.data.models.entities.notification.Notification
import com.cdek.courier.databinding.FragmentNotificationBinding
import com.cdek.courier.ui.base.notification.adapter.NotificationAdapter
import com.cdek.courier.utils.TASK_NUMBER
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_notification.*
import javax.inject.Inject

class NotificationFragment : DaggerFragment(), NotificationAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(
            this@NotificationFragment,
            viewModelFactory
        )[NotificationViewModel::class.java]
    }

    private val rvAdapter = NotificationAdapter(arrayListOf(), this@NotificationFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNotificationBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvNotification.layoutManager = LinearLayoutManager(activity)
        rvNotification.setHasFixedSize(true)
        rvNotification.adapter = rvAdapter
        initObserve()
    }

    private fun initObserve() {
        viewModel.getNotification()
            .observe(this@NotificationFragment, Observer<List<Notification>> {
                it?.let {
                    rvAdapter.setData(it)
                }
            })
    }

    override fun onItemClick(item: Notification) {
        viewModel.notificationHandled(item)
        val bundle = bundleOf(TASK_NUMBER to item.taskNumber)
        findNavController().navigate(R.id.action_notificationFragment_to_taskItem, bundle)
    }
}