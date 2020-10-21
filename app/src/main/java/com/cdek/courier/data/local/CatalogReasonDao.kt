package com.cdek.courier.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cdek.android.kotlinapp.source.models.entities.CatalogReason

@Dao
interface CatalogReasonDao {

    @Query("SELECT * FROM reasons")
    fun getAll(): List<CatalogReason>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<CatalogReason>)
}