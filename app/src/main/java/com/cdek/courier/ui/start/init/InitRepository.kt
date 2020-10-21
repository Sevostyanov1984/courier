package com.cdek.courier.ui.start.init

import com.cdek.courier.data.Repository
import com.cdek.courier.data.local.NotificationDao
import com.cdek.courier.data.local.TaskDao
import com.cdek.courier.data.models.auth.TokenRequest
import com.cdek.courier.data.models.auth.TokenResponse
import com.cdek.courier.data.network.Api
import com.cdek.courier.data.network.ApiResponse
import com.cdek.courier.utils.*
import javax.inject.Inject

class InitRepository @Inject constructor(
    api: Api,
    taskDao: TaskDao,
    notificationDao: NotificationDao
) : Repository(api, taskDao, notificationDao) {

    //    preference

    fun setPrefServer() {
//        TODO return prod
//        setPreferenceString(SCAN_PVZ_PREF_KEY_SERVER, SCAN_PVZ_SERVER_BASE)
        setPreferenceString(SCAN_PVZ_PREF_KEY_SERVER, SCAN_PVZ_SERVER_DEV)
    }

    //    Api

    suspend fun checkToken(token: String): ApiResponse<TokenResponse?> {
        val url = getPrefServer() + SERVER_ENDPOINT_TOKEN
        return responseApi(api.checkToken(url, TokenRequest(token)))
    }

}