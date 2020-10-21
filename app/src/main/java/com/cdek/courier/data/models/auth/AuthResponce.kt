package com.cdek.courier.data.models.auth

import com.google.gson.annotations.SerializedName

data class AuthResponce(
    @field:SerializedName("login") var login: String,
    @field:SerializedName("password") var password: String,
    @field:SerializedName("token") var token: String,
    @field:SerializedName("userId") var userId: String,
    @field:SerializedName("lang") var lang: String,
    @field:SerializedName("role") var role: List<String>
)