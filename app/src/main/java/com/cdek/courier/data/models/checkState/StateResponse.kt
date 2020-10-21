package com.cdek.courier.data.models.checkState

import com.google.gson.annotations.SerializedName

class StateResponse(
    @field:SerializedName("state")
    var isState: Boolean
) {

    @SerializedName("date")
    var date: String? = null
}