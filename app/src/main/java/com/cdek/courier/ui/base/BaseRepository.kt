package com.cdek.courier.ui.base

import com.cdek.courier.data.Repository
import com.cdek.courier.data.local.NotificationDao
import com.cdek.courier.data.local.TaskDao
import com.cdek.courier.data.network.Api
import javax.inject.Inject


class BaseRepository @Inject constructor(
    api: Api,
    taskDao: TaskDao,
    notificationDao: NotificationDao
) : Repository(api, taskDao, notificationDao) {

    //    preference

    //    Api

}