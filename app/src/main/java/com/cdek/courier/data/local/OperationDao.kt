package com.cdek.courier.data.local

import androidx.room.*
import com.cdek.courier.data.models.entities.Operation

@Dao
interface OperationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperation(data: Operation)

    @Query("SELECT * FROM operation WHERE taskNumber = :taskNumber")
    suspend fun getOperation(taskNumber: String): Operation

    @Query("DELETE FROM operation WHERE taskNumber = :taskNumber")
    suspend fun deleteByTaskNumber(taskNumber: String)
}