package com.cdek.courier.data.models.entities.task

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AdditionalService(
    @field:SerializedName("name") var name: String? = null,
    @field:SerializedName("price") var price: String? = null
) : Serializable