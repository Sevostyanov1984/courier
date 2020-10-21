package com.cdek.courier.ui.base

import androidx.lifecycle.*
import com.cdek.courier.data.models.entities.notification.Notification
import javax.inject.Inject


class BaseViewModel @Inject constructor(private val repository: BaseRepository) : ViewModel() {
    //    Events

    // bell
    private val _countNotification = MutableLiveData<Int>()

    val countNotification: LiveData<Int>
        get() = _countNotification

    fun getNotification() = repository.getNotificationList()

    fun handleUpdateNotificationList(list: List<Notification>) {
        var count = 0
        for (element in list) {
            if (!element.stateReaded) {
                count++
            }
        }
        _countNotification.postValue(count)
    }
}