package com.cdek.courier.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "updates")
data class UpdateEntity(
    @PrimaryKey var id: Int,
    var entityName: String,
    var version_id: String,
    var updateTime: String
)