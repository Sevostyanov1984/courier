package com.cdek.courier.data.models.auth

import com.google.gson.annotations.SerializedName

class TokenRequest(
    @field:SerializedName("token")
    var token: String?
)