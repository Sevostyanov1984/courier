package com.cdek.courier.ui.base.gallery

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cdek.courier.data.models.entities.FileEntity
import com.cdek.courier.data.models.entities.task.Task
import com.cdek.courier.utils.MESSAGE_ATTACH_SCAN_OK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class GalleryViewModel @Inject constructor(private val repository: GalleryRepository) :
    ViewModel() {

    //    Events

    private val _navigateToCamera = MutableLiveData<Boolean>()
    val navigateToCamera: LiveData<Boolean>
        get() = _navigateToCamera

    private val _navigateToTask = MutableLiveData<Boolean>()
    val navigateToTask: LiveData<Boolean>
        get() = _navigateToTask

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun navigateToCameraHandled() {
        _navigateToCamera.value = false
    }

    fun navigateToTaskHandled() {
        _navigateToTask.value = false
    }

    fun messageHandled() {
        _message.value = ""
    }

    private val _photoList = MutableLiveData<List<FileEntity>>()

    fun photoList() = _photoList as LiveData<List<FileEntity>>

    val task = ObservableField<Task>()

    val selectedItem = ObservableField<FileEntity>()

    lateinit var imagePath: String

    private fun fetchPhotoList() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = repository.getPhotoList(task.get()!!)
            // if db empty -> init fetchRemote
            if (list.isNullOrEmpty()) {
                _navigateToCamera.postValue(true)
            } else {
                _photoList.postValue(list)
                selectedItem.set(list[0])
            }
        }
    }

    fun fetchTask(taskNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            task.set(repository.getTask(taskNumber))
            fetchPhotoList()
        }
    }


    fun saveFileInfo(imagePath: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertFile(task.get()!!, imagePath)
            fetchPhotoList()
        }
    }

    fun addPhoto() {
        _navigateToCamera.postValue(true)
    }

    fun deletePhoto() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteFile(selectedItem.get())
            fetchPhotoList()
        }
    }

    fun sendPhoto() {
//        todo change state @readySend@
        _message.postValue(MESSAGE_ATTACH_SCAN_OK)
        _navigateToTask.postValue(true)
    }
}