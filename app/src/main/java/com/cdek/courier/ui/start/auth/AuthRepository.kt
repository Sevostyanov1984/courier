package com.cdek.courier.ui.start.auth

import com.cdek.courier.data.Repository
import com.cdek.courier.data.local.NotificationDao
import com.cdek.courier.data.local.TaskDao
import com.cdek.courier.data.models.auth.AuthRequest
import com.cdek.courier.data.models.auth.AuthResponce
import com.cdek.courier.data.models.auth.AutocompleteResponse
import com.cdek.courier.data.network.Api
import com.cdek.courier.data.network.ApiResponse
import com.cdek.courier.utils.*
import javax.inject.Inject

class AuthRepository @Inject constructor(
    api: Api,
    taskDao: TaskDao,
    notificationDao: NotificationDao
) : Repository(api, taskDao, notificationDao) {

    //    preference

    fun setPrefLogin(data: String) {
        setPreferenceString(SCAN_PVZ_PREF_KEY_USER_LOGIN, data)
    }

    fun setPrefUserId(data: String) {
//        TODO replace
        setPreferenceString(SCAN_PVZ_PREF_KEY_USERID, "45932")
//        setPreferenceString(SCAN_PVZ_PREF_KEY_USERID, data)
    }

    fun setPrefToken(data: String) {
        setPreferenceString(SCAN_PVZ_PREF_KEY_TOKEN, data)
    }

    fun setPrefLang(data: String) {
        setPreferenceString(SCAN_PVZ_PREF_KEY_LANG, data)
    }

    fun setPrefServer(data: String) {
        setPreferenceString(SCAN_PVZ_PREF_KEY_SERVER, data)
    }

    //    Api

    suspend fun getUserList(name: String): ApiResponse<AutocompleteResponse?> {
        val url = getPrefServer() + SERVER_ENDPOINT_AUTOCOMPLETE
        return responseApi(api.getUserList(url, name))
    }

    suspend fun logIn(auth: AuthRequest): ApiResponse<AuthResponce?> {
        val url = getPrefServer() + SERVER_ENDPOINT_AUTH
        return responseApi(api.logIn(url, auth))
    }

}