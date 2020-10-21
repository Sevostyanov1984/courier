package com.cdek.courier.data.models.entities.task

import com.google.gson.annotations.SerializedName

data class NotificationTask(
    @field:SerializedName("goodName") var goodName: String
)