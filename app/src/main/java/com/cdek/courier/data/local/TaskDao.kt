package com.cdek.courier.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cdek.courier.data.models.entities.task.Task

@Dao
interface TaskDao {

    @Transaction
    suspend fun replace(data: List<Task>) {
        deleteAll()
        insertList(data)
    }

    @Insert
    suspend fun insertList(data: List<Task>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(data: Task)

    //select date, basisNumber, taskState, taskType, urgency, courierId from task WHERE courierId = 45932 order by taskState in ('COMPLETE','NOT_COMPLETE'), urgency desc, taskState
    /* @Query("SELECT t.*, " +
             "(select stateReaded from notification as n where n.taskNumber = t.basisNumber order by stateReaded limit 1) as 'notification' " +
             "from task t " +
             "WHERE courierId = :courierId " +
             "ORDER BY date DESC")*/
    @Query(
        "SELECT t.*, " +
                "(select stateReaded from notification as n where n.taskNumber = t.basisNumber order by stateReaded limit 1) as 'notification' " +
                "from task t " +
                "WHERE courierId = :courierId " +
                "order by taskState in ('COMPLETE','NOT_COMPLETE'), urgency desc, taskState"
    )
    //@Query("SELECT * FROM task WHERE courierId = :courierId ORDER BY date DESC")
    fun getAll(courierId: String): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE basisNumber = :taskNumber and courierId = :courierId")
    suspend fun getTask(taskNumber: String, courierId: String): Task

    @Query("SELECT * FROM task WHERE basisNumber = :taskNumber and courierId = :courierId")
    fun getMyTask(taskNumber: String?, courierId: String): LiveData<Task>

    @Query("UPDATE task SET taskState = :state WHERE basisNumber = :taskNumber and courierId = :courierId")
    suspend fun updateTaskState(taskNumber: String, state: String, courierId: String)

    @Delete
    suspend fun delete(data: Task)

    @Query("DELETE FROM task")
    suspend fun deleteAll()
}