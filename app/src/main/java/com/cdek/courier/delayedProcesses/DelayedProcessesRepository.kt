package com.cdek.courier.delayedProcesses

import com.cdek.courier.data.Repository
import com.cdek.courier.data.local.NotificationDao
import com.cdek.courier.data.local.TaskDao
import com.cdek.courier.data.network.Api
import com.cdek.courier.utils.SCAN_PVZ_PREF_KEY_FCM_TOKEN
import com.cdek.courier.utils.setPreferenceString
import javax.inject.Inject

class DelayedProcessesRepository @Inject constructor(
    api: Api,
    taskDao: TaskDao,
    notificationDao: NotificationDao
) :
    Repository(api, taskDao, notificationDao) {

    //    preference

    fun setPrefFCMToken(token: String) {
        setPreferenceString(SCAN_PVZ_PREF_KEY_FCM_TOKEN, token)
    }

    //    Api

}