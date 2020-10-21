package com.cdek.courier.data.models.entities.task

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Note(
    @field:SerializedName("time") var time: String?,
    @field:SerializedName("text") var text: String?
) : Serializable