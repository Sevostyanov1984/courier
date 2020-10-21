package com.cdek.courier.data.models

import com.google.gson.annotations.SerializedName

data class UserAuth(
    @SerializedName("login") var login: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("id") var id: Long? = null,
    @SerializedName("sessionID") var sessionID: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("cityId") var lang: Long? = null,
    @SerializedName("role") var role: List<String>? = null
)