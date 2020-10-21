package com.cdek.courier.data.models

import com.google.gson.annotations.SerializedName

data class AuthData(
    @SerializedName("login") var login: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("token") var token: String? = null,
    @SerializedName("userId") var userId: String? = null,
    @SerializedName("lang") var lang: String? = null,
    @SerializedName("role") var role: List<String>? = null
)
