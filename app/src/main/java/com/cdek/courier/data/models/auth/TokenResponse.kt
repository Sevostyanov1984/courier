package com.cdek.courier.data.models.auth

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @field:SerializedName("login") var login: String?,
    @field:SerializedName("blocked") var blocked: Boolean?
)