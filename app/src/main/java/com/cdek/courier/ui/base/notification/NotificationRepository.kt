package com.cdek.courier.ui.base.notification

import com.cdek.courier.data.Repository
import com.cdek.courier.data.local.NotificationDao
import com.cdek.courier.data.local.TaskDao
import com.cdek.courier.data.models.entities.notification.Notification
import com.cdek.courier.data.network.Api
import com.cdek.courier.utils.STATE_READED_NOTIFICATION
import javax.inject.Inject

open class NotificationRepository @Inject constructor(
    api: Api,
    notificationDao: NotificationDao,
    taskDao: TaskDao
) : Repository(api, taskDao, notificationDao) {

    //    preference

    //    Api


    //    local

    suspend fun setReaded(item: Notification) {
        notificationDao.setReaded(item.id!!, STATE_READED_NOTIFICATION)
    }

    suspend fun deleteNotification(notification: Notification) {
        notificationDao.delete(notification)
    }
}