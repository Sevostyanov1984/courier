package com.cdek.courier.data

import androidx.lifecycle.LiveData
import com.cdek.courier.data.local.NotificationDao
import com.cdek.courier.data.local.TaskDao
import com.cdek.courier.data.models.auth.AutocompleteResponse
import com.cdek.courier.data.models.checkState.StateResponse
import com.cdek.courier.data.models.entities.notification.Notification
import com.cdek.courier.data.models.entities.notification.NotificationRequest
import com.cdek.courier.data.models.entities.task.Task
import com.cdek.courier.data.models.entities.task.TaskRequest
import com.cdek.courier.data.models.firebase.FcmRequest
import com.cdek.courier.data.network.Api
import com.cdek.courier.data.network.ApiResponse
import com.cdek.courier.utils.*
import retrofit2.Response
import javax.inject.Inject


open class Repository @Inject constructor(
    val api: Api,
    val taskDao: TaskDao,
    val notificationDao: NotificationDao
) {

//    preference

    fun getPrefToken(): String {
        return getPreferenceString(SCAN_PVZ_PREF_KEY_TOKEN)
    }

    fun getPrefFCMToken(): String {
        return getPreferenceString(SCAN_PVZ_PREF_KEY_FCM_TOKEN)
    }

    fun getPrefLang(): String {
        return getPreferenceString(SCAN_PVZ_PREF_KEY_LANG)
    }

    fun getPrefLogin(): String {
        return getPreferenceString(SCAN_PVZ_PREF_KEY_USER_LOGIN)
    }

    fun getPrefUserId(): String {
        return getPreferenceString(SCAN_PVZ_PREF_KEY_USERID)
    }

    fun getPrefServer(): String {
        return getPreferenceString(SCAN_PVZ_PREF_KEY_SERVER)
    }

//    Api

    fun <T> responseApi(response: Response<T>): ApiResponse<T?> {
        return if (response.isSuccessful)
            ApiResponse.Success(response.body())
        else
            ApiResponse.Failure(response.message(), response.code())
    }

    suspend fun getTaskRemote(): ApiResponse<List<Task>?> {
        val url = getPrefServer() + SERVER_ENDPOINT_TASK
        val data = TaskRequest(getPrefUserId(), getPrefLang(), getPrefToken())
        return responseApi(api.getTaskRemote(url, data))
    }

    suspend fun sendFCMToken(fcmRequest: FcmRequest): ApiResponse<StateResponse?> {
        val url = getPrefServer() + SERVER_ENDPOINT_TOKEN
        return responseApi(api.sendFCMToken(url, fcmRequest))
    }

    suspend fun getUserListTemp(url: String, name: String): ApiResponse<AutocompleteResponse?> {
        return responseApi(api.getUserList(url, name))
    }

    suspend fun getNotificationRemote(): ApiResponse<List<Notification>?> {
        val url = getPrefServer() + SERVER_ENDPOINT_NOTIFICATION
        return responseApi(api.getNotificationRemote(url, NotificationRequest(getPrefUserId())))
    }

    suspend fun getAll(): String {
        var message = ""
        //get Task list
        when (val response = getTaskRemote(
        )) {
            is ApiResponse.Success -> {
                response.body?.let {
                    insertTaskList(response.body)
                }
            }
            is ApiResponse.Failure -> {
                message += response.message
            }
        }

//        get Notification list
        when (val response = getNotificationRemote(
        )) {
            is ApiResponse.Success -> {
                response.body?.let {
                    insertNotificationList(response.body)
                }
            }
            is ApiResponse.Failure -> {
                message += response.message
            }
        }
        return message
    }


    //    local
    suspend fun getTask(taskNumber: String): Task {
        return taskDao.getTask(taskNumber, getPrefUserId())
    }

    fun getMyTask(taskNumber: String?): LiveData<Task> {
        return taskDao.getMyTask(taskNumber, getPrefUserId())
    }

    suspend fun updateTaskState(taskNumber: String, state: String) {
        return taskDao.updateTaskState(taskNumber, state, getPrefUserId())
    }

    fun getNotificationList(): LiveData<List<Notification>> {
        return notificationDao.getAll()
    }

    suspend fun insertTaskList(data: List<Task>) {
        taskDao.replace(data)
    }

    suspend fun insertNotificationList(list: List<Notification>) {
        notificationDao.insert(list)
    }
}