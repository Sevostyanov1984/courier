package com.cdek.courier.ui.photo.sign

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cdek.courier.data.models.entities.Operation
import com.cdek.courier.data.models.entities.task.Task
import com.cdek.courier.data.models.packageInfo.PackageInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class SignViewModel @Inject constructor(private val repository: SignRepository) : ViewModel() {

    //    Events

    private val _clearCanvas = MutableLiveData<Boolean>()
    val clearCanvas: LiveData<Boolean>
        get() = _clearCanvas

    fun clearCanvasHandled() {
        _clearCanvas.value = false
    }

    private val _acceptSign = MutableLiveData<Boolean>()
    val acceptSign: LiveData<Boolean>
        get() = _acceptSign

    fun acceptSignHandled() {
        _acceptSign.value = false
    }


    var isPainting = ObservableBoolean()

    val taskOperation = ObservableField<Operation>()

    val task = ObservableField<Task>()

    val stateDelivery = ObservableField<String>()

    val packageInfo = ObservableField<PackageInfo>()

    var isHanded = ObservableBoolean()

    fun fetchTask(taskNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            isPainting.set(true)
            task.set(repository.getTask(taskNumber))
            taskOperation.set(repository.getOperation(task.get()!!))
            taskOperation.get()?.causeRefuse?.isEmpty()?.let {
                isHanded.set(true)
            }
            prepareText()
        }
    }

    //    prepare text for view
    private fun prepareText() {
        packageInfo.set(PackageInfo(task.get()?.places))
        //if (order.getStateRefuse() != Constants.HANDED)
        if (isHanded.get()) {
            stateDelivery.set(
                if (taskOperation.get()!!.isPart!!) {
                    "частично"
                } else {
                    "полностью"
                }
            )
        } else {
            stateDelivery.set(taskOperation.get()!!.causeRefuse)
        }
    }

    fun clear() {
        _clearCanvas.postValue(true)
    }

    fun onAcceptSign() {
        _acceptSign.postValue(true)
    }
}