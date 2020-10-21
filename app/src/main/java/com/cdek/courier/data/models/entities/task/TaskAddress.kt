package com.cdek.courier.data.models.entities.task

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TaskAddress(
    @field:SerializedName("targetAddress") var targetAddress: String?,
    @field:SerializedName("latitude") var latitude: String?,
    @field:SerializedName("longitude") var longitude: String?
) : Serializable