package com.cdek.courier.ui.base.notification

import androidx.lifecycle.ViewModel
import com.cdek.courier.data.models.entities.notification.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class NotificationViewModel @Inject constructor(private val repository: NotificationRepository) :
    ViewModel() {

    fun getNotification() = repository.getNotificationList()

    fun notificationHandled(item: Notification) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.setReaded(item)
        }
    }
}