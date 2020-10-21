package com.cdek.courier.data.models.entities.task

import com.google.gson.annotations.SerializedName

data class Good(
    @field:SerializedName("ec4Id") var ec4Id: Int?,
    @field:SerializedName("vendorCode") var vendorCode: String?,
    @field:SerializedName("packNumber") var packNumber: String?,
    @field:SerializedName("name") var name: String?,
    @field:SerializedName("price") var price: String?,
    @field:SerializedName("priceToPay") var priceToPay: String?,
    @field:SerializedName("weight") var weight: String?,
    @field:SerializedName("count") var count: Int?,
    @field:SerializedName("countDelivered") var countDelivered: Int?,
    @field:SerializedName("codeTnVed") var codeTnVed: String?,
    @field:SerializedName("url") var url: String?,
    @field:SerializedName("vatRateCode") var vatRateCode: String?,
    @field:SerializedName("vatSum") var vatSum: Double?,
    @field:SerializedName("notification") var notificationTask: NotificationTask?,
    @field:SerializedName("nameEx") var nameEx: String?,
    @field:SerializedName("grosWeightPerUnit") var grosWeightPerUnit: String?,
    @field:SerializedName("markCode") var markCode: String?
)