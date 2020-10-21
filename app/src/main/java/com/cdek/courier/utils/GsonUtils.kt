package com.cdek.courier.utils

import com.google.gson.GsonBuilder
import java.util.Collections.emptyList


object GsonUtils {
    private val gsonBuilder: GsonBuilder = GsonBuilder()

    fun converToJson(convertObject: Any?): String? {
        return if (convertObject == null) {
            null
        } else {
            gsonBuilder.create().toJson(convertObject)
        }
    }

    fun <T> convertFromJson(json: String?, objectClass: Class<T>): T? {
        return gsonBuilder.create().fromJson(json, objectClass)
    }

    fun <T> convertArrayFromJson(json: String?, type: Class<Array<T>>): MutableList<T>? {
        return if (json == null) {
            emptyList()
        } else {
            gsonBuilder.create().fromJson(json, type).toMutableList()
        }
    }
}