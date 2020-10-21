package com.cdek.courier.data.network

import com.cdek.android.kotlinapp.source.models.entities.CatalogReason
import com.cdek.android.kotlinapp.source.models.entities.OrderOld
import com.cdek.courier.data.models.auth.*
import com.cdek.courier.data.models.checkState.StateResponse
import com.cdek.courier.data.models.entities.notification.Notification
import com.cdek.courier.data.models.entities.notification.NotificationRequest
import com.cdek.courier.data.models.entities.task.Task
import com.cdek.courier.data.models.entities.task.TaskRequest
import com.cdek.courier.data.models.firebase.FcmRequest
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @GET("ping")
    suspend fun getPing(): String

    @POST
    suspend fun checkToken(@Url url: String, @Body token: TokenRequest): Response<TokenResponse>

    @POST
    suspend fun getTaskRemote(@Url url: String, @Body data: TaskRequest): Response<List<Task>>

    @POST
    suspend fun sendFCMToken(@Url url: String, @Body data: FcmRequest): Response<StateResponse>

    @GET
    suspend fun getReason(@Url url: String, @Query("catalogType") catalogType: String): List<CatalogReason>

    @GET
    suspend fun getUserList(@Url url: String, @Query("name") name: String): Response<AutocompleteResponse>

    @POST
    suspend fun logIn(@Url url: String, @Body auth: AuthRequest): Response<AuthResponce>

    @POST
    suspend fun getNotificationRemote(@Url url: String, @Body data: NotificationRequest): Response<List<Notification>>

}