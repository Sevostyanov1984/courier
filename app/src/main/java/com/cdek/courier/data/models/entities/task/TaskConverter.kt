package com.cdek.courier.data.models.entities.task

import androidx.room.TypeConverter
import com.cdek.courier.utils.GsonUtils
import java.util.*


class TaskConverter {
    @TypeConverter
    fun fromCargo(data: Cargo?): String? {
        return GsonUtils.converToJson(data)
    }

    @TypeConverter
    fun toCargo(data: String): Cargo? {
        return GsonUtils.convertFromJson(data, Cargo::class.java)
    }

    @TypeConverter
    fun toPlace(data: String?): List<Place?>? {
        return GsonUtils.convertArrayFromJson(data, Array<Place>::class.java)
    }

    @TypeConverter
    fun fromPlace(data: List<Place?>?): String? {
        return GsonUtils.converToJson(data)
    }

    @TypeConverter
    fun toGood(data: String?): List<Good?>? {
        return GsonUtils.convertArrayFromJson(data, Array<Good>::class.java)
    }

    @TypeConverter
    fun fromGood(data: List<Good?>?): String? {
        return GsonUtils.converToJson(data)
    }

    @TypeConverter
    fun toNote(data: String?): List<Note?>? {
        return GsonUtils.convertArrayFromJson(data, Array<Note>::class.java)
    }

    @TypeConverter
    fun fromNote(data: List<Note?>?): String? {
        return GsonUtils.converToJson(data)
    }

    @TypeConverter
    fun toPhone(data: String?): List<Phone?>? {
        return GsonUtils.convertArrayFromJson(data, Array<Phone>::class.java)
    }

    @TypeConverter
    fun fromPhone(data: List<Phone?>?): String? {
        return GsonUtils.converToJson(data)
    }

    @TypeConverter
    fun toAdditionalService(data: String?): List<AdditionalService?>? {
        return GsonUtils.convertArrayFromJson(data, Array<AdditionalService>::class.java)
    }

    @TypeConverter
    fun fromAdditionalService(data: List<AdditionalService?>?): String? {
        return GsonUtils.converToJson(data)
    }

    @TypeConverter
    fun toTaskAddress(data: String?): TaskAddress? {
        return GsonUtils.convertFromJson(data, TaskAddress::class.java)
    }

    @TypeConverter
    fun fromTaskAddress(data: TaskAddress): String? {
        return GsonUtils.converToJson(data)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}