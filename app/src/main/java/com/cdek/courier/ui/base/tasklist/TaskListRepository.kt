package com.cdek.courier.ui.base.tasklist

import androidx.lifecycle.LiveData
import com.cdek.courier.data.Repository
import com.cdek.courier.data.local.NotificationDao
import com.cdek.courier.data.local.TaskDao
import com.cdek.courier.data.models.entities.task.Task
import com.cdek.courier.data.network.Api
import javax.inject.Inject

class TaskListRepository @Inject constructor(
    api: Api,
    taskDao: TaskDao,
    notificationDao: NotificationDao
) : Repository(api, taskDao, notificationDao) {
    //    preference

    //    Api


    //    local


    suspend fun insertTask(data: Task) {
        taskDao.insertTask(data)
    }

    fun getTaskList(): LiveData<List<Task>> {
        return taskDao.getAll(getPrefUserId())
    }

    suspend fun deleteTask(data: Task) {
        taskDao.delete(data)
    }
}