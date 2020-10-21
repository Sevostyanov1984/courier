package com.cdek.courier.ui.base.task

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cdek.courier.data.models.auth.item.Item
import com.cdek.courier.data.models.entities.task.Task
import com.cdek.courier.data.network.ApiResponse
import com.cdek.courier.utils.enums.LoadingTypes
import com.cdek.courier.utils.enums.TaskStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    // естессно берем самый кривой вариант из нижеперечисленных
    //https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150?

    //    Events
    private val _navigateToSign = MutableLiveData<Boolean>()
    val navigateToSign: LiveData<Boolean>
        get() = _navigateToSign

    private val _navigateToGallery = MutableLiveData<Boolean>()
    val navigateToGallery: LiveData<Boolean>
        get() = _navigateToGallery

    private val _navigateToPartial = MutableLiveData<Boolean>()
    val navigateToPartial: LiveData<Boolean>
        get() = _navigateToPartial

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun navigateToSignHandled() {
        _navigateToSign.value = false
    }

    fun navigateToGalleryHandled() {
        _navigateToGallery.value = false
    }

    fun navigateToPartialHandled() {
        _navigateToPartial.value = false
    }

    fun messageHandled() {
        _message.value = ""
    }

    var eventFired: Boolean = false


    var isLoading = MutableLiveData<LoadingTypes>()

    fun updateLoading() {

        CoroutineScope(Dispatchers.IO).launch {
            val ss = task?.get()?.taskState
            when (ss) {
                TaskStates.ADDED.toString() -> {
                    isLoading.postValue(LoadingTypes.START)
                    delay(1000)
                    repository.updateTaskState(task?.get()?.basisNumber!!, "READED")
                    eventFired = true
                    isLoading.postValue(LoadingTypes.END)
                }
                TaskStates.RECEIVE.toString() -> {
                    _navigateToSign.postValue(true)
                }
            }
        }
    }

    fun getMyTask(taskNumber: String?): LiveData<Task> {
        isLoading.postValue(LoadingTypes.START)

        // механизм добавления в livedata виджета загрузки если тянем данные из локального хранилища
        return Transformations.map(repository.getMyTask(taskNumber)) { _task ->
            task?.set(_task)
            taskState?.set(_task.taskState)
            val sb = StringBuilder()
            for ((index, note) in _task?.notes?.withIndex()!!) {
                sb.append(note.time).append(note.text)
                if (index != _task.notes?.size?.minus(1)) {
                    sb.append(System.lineSeparator())
                }
            }
            notes.set(sb.toString())
            isLoading.postValue(LoadingTypes.END)
            _task
        }
    }

    private val _problemList = mutableListOf<String>()
    val problemList: List<String>
        get() = _problemList.toList()

    val task: ObservableField<Task>? = ObservableField<Task>()
    val notes = ObservableField<String>()
    val taskState: ObservableField<String>? = ObservableField<String>().also {
        it.set(task?.get()?.taskState)
    }

    fun fetchProblems() {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = repository.getUserListTemp(
                "https://scanpvzback.cdek.ru/auth/autocomplete",
                "пар"
            )) {
                is ApiResponse.Success -> {
                    for (item: Item in response.body!!.items!!)
                        _problemList.add(item.login!!)
                }
                is ApiResponse.Failure -> {
                    _message.postValue(response.message)
                }
            }
        }
    }

    fun addSign() {
        //    todo temporally
//        val sign = task.get()?.enableElectronicSignature
//        if(sign != null && sign){
        _navigateToSign.postValue(true)
//        } else {
//            _navigateToPhoto.postValue(true)
//        }
    }

    //    todo temporally
    fun addPhoto() {
        _navigateToGallery.postValue(true)
    }

    fun showPartial() {
        _navigateToPartial.postValue(true)
    }

    fun sendingData() {
        CoroutineScope(Dispatchers.IO).launch {
            isLoading.postValue(LoadingTypes.START)
            delay(1000)
            repository.updateTaskState(task?.get()?.basisNumber!!, "EXECUTED")
            eventFired = true
            isLoading.postValue(LoadingTypes.END)
        }
    }
}