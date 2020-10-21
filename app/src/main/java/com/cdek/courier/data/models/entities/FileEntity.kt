package com.cdek.courier.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file")
data class FileEntity(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    var taskNumber: String?,
    var filePath: String?,
    var readySend: Boolean?,
    var posted: Boolean = false
)