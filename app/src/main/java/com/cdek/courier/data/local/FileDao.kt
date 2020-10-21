package com.cdek.courier.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cdek.courier.data.models.entities.FileEntity

@Dao
interface FileDao {

    @Insert
    suspend fun insertFile(fileEntity: FileEntity)

    @Query("SELECT * FROM file WHERE taskNumber =:taskNumber and posted = :posted")
    suspend fun getPhotoList(posted: Int, taskNumber: String): List<FileEntity>

    @Query("SELECT filePath FROM file WHERE posted = :posted")
    suspend fun getPathListForDelete(posted: Int): List<String>?

    @Query("UPDATE file SET posted = :posted WHERE taskNumber = :taskNumber")
    suspend fun updateFile(posted: Int, taskNumber: String)

    @Query("DELETE FROM file WHERE posted = :posted")
    suspend fun deleteFile(posted: Int)

    @Query("DELETE FROM file WHERE taskNumber = :taskNumber")
    suspend fun deleteFileByTaskNumber(taskNumber: String)

    @Delete
    suspend fun deleteFile(fileEntity: FileEntity?)

//    @Query("DELETE FROM orders WHERE number = :orderNumber")
//    fun deleteOrdersByOrderNumber(orderNumber: String?)
}