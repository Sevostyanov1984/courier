package com.cdek.courier.data.models.entities

import androidx.room.TypeConverter
import com.cdek.courier.data.models.Packages
import com.cdek.courier.utils.GsonUtils.converToJson
import com.cdek.courier.utils.GsonUtils.convertArrayFromJson


class PackagesConverter {
    @TypeConverter
    fun fromPackages(packages: MutableList<Packages>): String? {
        return converToJson(packages)
    }

    @TypeConverter
    fun toPackages(data: String): List<Packages>? {
        return convertArrayFromJson(data, Array<Packages>::class.java)
    }
}