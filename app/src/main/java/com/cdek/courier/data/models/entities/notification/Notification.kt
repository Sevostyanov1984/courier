package com.cdek.courier.data.models.entities.notification

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cdek.courier.utils.CREATE_DATE_TEMPLATE
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "notification")
data class Notification(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    var stateReaded: Boolean = false,
    var date: String? = SimpleDateFormat(
        CREATE_DATE_TEMPLATE,
        Locale.getDefault()
    ).format(Date()),
    @field:SerializedName("typeEvent") var typeEvent: String?,
    @field:SerializedName("taskNumber") var taskNumber: String?,
    @field:SerializedName("taskUUID") var taskUUID: String?,
    @field:SerializedName("entity") var entity: String?,
    @field:SerializedName("message") var message: String?,
    @field:SerializedName("courierID") var courierID: String?
)