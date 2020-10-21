package com.cdek.courier.data.models.auth

import com.cdek.courier.data.models.auth.item.Item
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AutocompleteResponse(@SerializedName("items") @Expose var items: List<Item>? = null)