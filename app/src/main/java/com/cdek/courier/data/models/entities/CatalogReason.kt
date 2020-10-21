package com.cdek.android.kotlinapp.source.models.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reasons")
class CatalogReason(
    @NonNull @PrimaryKey var id: String,
    var code: String?,
    var name: String?,
    var lang: String?,
    var active: Boolean?
) {
}