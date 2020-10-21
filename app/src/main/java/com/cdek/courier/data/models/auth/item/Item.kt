package com.cdek.courier.data.models.auth.item

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("login") @Expose var login: String? = null,
    @SerializedName("blocked") var blocked: Boolean? = null
)