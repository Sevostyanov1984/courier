package com.cdek.courier.ui.base.gallery

import com.cdek.courier.data.Repository
import com.cdek.courier.data.local.FileDao
import com.cdek.courier.data.local.NotificationDao
import com.cdek.courier.data.local.TaskDao
import com.cdek.courier.data.models.entities.FileEntity
import com.cdek.courier.data.models.entities.task.Task
import com.cdek.courier.data.network.Api
import com.cdek.courier.utils.BOOLEAN_FALSE
import javax.inject.Inject

class GalleryRepository @Inject constructor(
    api: Api,
    taskDao: TaskDao,
    notificationDao: NotificationDao,
    private val fileDao: FileDao
) : Repository(api, taskDao, notificationDao) {

    //    preference

    //    Api

    //    local

    suspend fun insertFile(task: Task, imagePath: String) {
        fileDao.insertFile(FileEntity(null, task.basisNumber!!, imagePath, false, false))
    }

    suspend fun getPhotoList(task: Task): List<FileEntity> {
        return fileDao.getPhotoList(BOOLEAN_FALSE, task.basisNumber!!)
    }

    suspend fun deleteFile(data: FileEntity?) {
        fileDao.deleteFile(data)
    }

}