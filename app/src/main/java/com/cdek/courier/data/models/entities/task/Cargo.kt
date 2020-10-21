package com.cdek.courier.data.models.entities.task

import com.google.gson.annotations.SerializedName

data class Cargo(
    @field:SerializedName("places") var places: List<Place>,
    @field:SerializedName("shopNumberDep") var shopNumberDep: String,
    @field:SerializedName("shopSellerName") var shopSellerName: String,
    @field:SerializedName("shopAdditionalCollection") var shopAdditionalCollection: String,
    @field:SerializedName("vatRateCode") var vatRateCode: String,
    @field:SerializedName("vatSum") var vatSum: Double
)