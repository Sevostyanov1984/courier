package com.cdek.courier.ui.photo.sign

import com.cdek.courier.data.Repository
import com.cdek.courier.data.local.NotificationDao
import com.cdek.courier.data.local.OperationDao
import com.cdek.courier.data.local.TaskDao
import com.cdek.courier.data.models.entities.Operation
import com.cdek.courier.data.models.entities.task.Task
import com.cdek.courier.data.network.Api
import javax.inject.Inject

class SignRepository @Inject constructor(
    api: Api,
    taskDao: TaskDao,
    notificationDao: NotificationDao,
    private val operationDao: OperationDao
) : Repository(api, taskDao, notificationDao) {

    //    preference

    //    Api

    //    local
    suspend fun getOperation(task: Task): Operation {
        var taskOperation = operationDao.getOperation(task.basisNumber!!)
        if (taskOperation == null) {
            taskOperation = Operation(1, task.basisNumber, task.taskState, "", true, "Pupkin Vasia")
            operationDao.insertOperation(taskOperation)
        }
        return taskOperation
    }

}