package com.cdek.courier.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cdek.courier.data.models.entities.notification.Notification
import com.cdek.courier.data.models.entities.task.Task


@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<Notification>)


    @Query("SELECT * FROM (SELECT * FROM notification ORDER BY date DESC) ORDER BY stateReaded")
    fun getAll(): LiveData<List<Notification>>

    @Query("UPDATE notification SET stateReaded = :stateReaded")
    suspend fun clear(stateReaded: Int)

    @Query("UPDATE notification SET stateReaded = :stateReaded WHERE id = :notificationId")
    suspend fun setReaded(notificationId: Int, stateReaded: Int)

    @Delete
    suspend fun delete(notification: Notification)
}