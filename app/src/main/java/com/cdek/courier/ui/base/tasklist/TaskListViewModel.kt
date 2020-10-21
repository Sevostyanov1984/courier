package com.cdek.courier.ui.base.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.databinding.ObservableBoolean


class TaskListViewModel @Inject constructor(private val repository: TaskListRepository) :
    ViewModel() {

    private val _message = MutableLiveData<String>()

    val message: LiveData<String>
        get() = _message

    fun messageHandled() {
        _message.value = ""
    }

    var isLoading = ObservableBoolean()


    fun getTasks() = repository.getTaskList()

    fun getAll() {
        CoroutineScope(Dispatchers.IO).launch {
            isLoading.set(true)
            val result = repository.getAll()
            if (result.isNotEmpty()) {
                _message.postValue(result)
            }
            isLoading.set(false)
        }
    }

    fun refresh() {
        getAll()
    }
}