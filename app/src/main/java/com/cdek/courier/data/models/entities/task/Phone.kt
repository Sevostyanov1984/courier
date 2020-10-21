package com.cdek.courier.data.models.entities.task

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Phone(
    @field:SerializedName("phoneTypeAlias") var phoneTypeAlias: String,
    @field:SerializedName("number") var number: String,
    @field:SerializedName("extNumber") var extNumber: String
) : Serializable