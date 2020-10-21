package com.cdek.courier.ui.base.partial

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cdek.courier.data.Repository
import com.cdek.courier.data.models.entities.task.Good
import com.cdek.courier.data.models.entities.task.Place
import com.cdek.courier.data.models.entities.task.Task
import com.cdek.courier.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class PartialViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //    Events

    private val _navigateToTask = MutableLiveData<Boolean>()
    val navigateToTask: LiveData<Boolean>
        get() = _navigateToTask

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun navigateToTaskHandled() {
        _navigateToTask.value = false
    }

    fun messageHandled() {
        _message.value = ""
    }

    var isLoading = ObservableBoolean()

    val task = ObservableField<Task>()

    val cost = ObservableField<Double>()
    val statePartial = ObservableField<String>()

    private val _place: ArrayList<Place> = ArrayList()

    private val _getPlace = MutableLiveData<List<Place>>()

    fun getPlace() = _getPlace as LiveData<List<Place>>

    fun fetchTask(taskNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            isLoading.set(true)
            task.set(repository.getTask(taskNumber))
            isLoading.set(false)
            prepareData()
        }
    }

    private fun prepareData() {
        var calculateCost = 0.0
        task.get()?.places?.let {
            for (place in it) {
                val parent = place.copy()
                if (place.goods != null) {
                    val listParcel: ArrayList<Good> = ArrayList()
                    for (parcel in place.goods!!) {
                        calculateCost += parcel.price!!.toDouble() * parcel.count!!
                        listParcel.add(parcel.copy())
                    }
                    parent.goods = listParcel
                }
                _place.add(parent)
            }
        }
        _getPlace.postValue(_place)
        statePartial.set(if (task.get()?.canPartDelivery!!) "доступна" else "запрещена")
        cost.set(calculateCost)
    }

    fun updateData(parent: Int, child: Int, operation: String) {
        val currentCost = cost.get()!!
        val priceParcel = task.get()!!.places!![parent].goods!![child].price!!.toDouble()
        var step = 0
        var state = false
        when (operation) {
            OPERATION_DECREMENT -> {
                step = DECREMENT
                state = decrementAmount(parent, child)
            }
            OPERATION_REDUCE -> {
                step = REDUCE
                state = reduceAmount(parent, child)
            }
            OPERATION_INCREMENT -> {
                step = INCREMENT
                state = incrementAmount(parent, child)
            }
            OPERATION_INCREASE -> {
                step = INCREASE
                state = increaseAmount(parent, child)
            }
        }
        if (state) {
            cost.set(currentCost.plus(priceParcel * step))
            _getPlace.postValue(_place)
        }
    }

    private fun updateAmount(parent: Int, child: Int, amount: Int): Boolean {
        val countInt: Int = _place[parent].goods!![child].count!!
        val needUpdate: Boolean = amount in 0..countInt
        if (needUpdate) _place[parent].goods!![child].countDelivered = amount
        return needUpdate
    }

    private fun getCountDelivered(parent: Int, child: Int): Int {
        return _place[parent].goods!![child].countDelivered!!
    }

    private fun decrementAmount(parent: Int, child: Int): Boolean {
        val amount: Int = getCountDelivered(parent, child) + DECREMENT
        return updateAmount(parent, child, amount)
    }

    private fun reduceAmount(parent: Int, child: Int): Boolean {
        val amount: Int = getCountDelivered(parent, child) + REDUCE
        return updateAmount(parent, child, amount)
    }

    private fun incrementAmount(parent: Int, child: Int): Boolean {
        val amount: Int = getCountDelivered(parent, child) + INCREMENT
        return updateAmount(parent, child, amount)
    }

    private fun increaseAmount(parent: Int, child: Int): Boolean {
        val amount: Int = getCountDelivered(parent, child) + INCREASE
        return updateAmount(parent, child, amount)
    }

    fun onFabClick() {
        if (task.get()?.canPartDelivery!!) {
//          todo add request initSurchargeDto
        } else {
            _navigateToTask.value = true
        }
    }

}