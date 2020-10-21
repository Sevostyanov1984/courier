package com.cdek.courier.data.models.entities.task

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Place(
    @field:SerializedName("id") var id: String?,
    @field:SerializedName("description") var description: String?,
    @field:SerializedName("packageNumber") var packageNumber: String?,
    @field:SerializedName("width") var width: Int?,
    @field:SerializedName("height") var height: Int?,
    @field:SerializedName("length") var length: Int?,
    @field:SerializedName("weight") var weight: Double?,
    @field:SerializedName("volumeWeight") var volumeWeight: Double?,
    @field:SerializedName("calcWeight") var calcWeight: Double?,
    @field:SerializedName("barcode") var barcode: String?,
    @field:SerializedName("goods") var goods: List<Good>?,
    @field:SerializedName("ec4Id") var ec4Id: Int?
) : Serializable