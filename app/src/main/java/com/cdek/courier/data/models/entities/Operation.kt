package com.cdek.courier.data.models.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cdek.courier.utils.CREATE_DATE_TEMPLATE
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "operation")
data class Operation(
    @NonNull @PrimaryKey(autoGenerate = true) var id: Int,
    var taskNumber: String?,
    var taskState: String?,
    var causeRefuse: String?,
    var isPart: Boolean?,
    var received: String?
) {
    var date: String = SimpleDateFormat(
        CREATE_DATE_TEMPLATE,
        Locale.getDefault()
    ).format(Date())
}