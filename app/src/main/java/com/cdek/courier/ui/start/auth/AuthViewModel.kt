package com.cdek.courier.ui.start.auth

import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cdek.courier.data.models.auth.AuthRequest
import com.cdek.courier.data.models.auth.AuthResponce
import com.cdek.courier.data.models.auth.item.Item
import com.cdek.courier.data.network.ApiResponse
import com.cdek.courier.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    //    Events

    private val _navigateToBase = MutableLiveData<Boolean>()
    val navigateToBase: LiveData<Boolean>
        get() = _navigateToBase

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun navigateToBaseHandled() {
        _navigateToBase.value = false
    }

    fun messageHandled() {
        _message.value = ""
    }

    var login = ObservableField<String>()
    var password = ObservableField<String>()
    var showSpinner = ObservableBoolean()
    var i = 1

    fun countClick() {
        i++
        if (i == 5)
            showSpinner.set(true)
    }

    fun checkLastAuth() {
//        Если есть прошлая удачная авторизация, вносим логин
        if (repository.getPrefLogin().isNotEmpty()) {
            login.set(repository.getPrefLogin())
        }
    }

    private val _getUsers = MutableLiveData<List<String>>()
        .also { fetchUsers() }

    fun getUsers() = _getUsers as LiveData<List<String>>?

    fun doSearchUsers() {
        fetchUsers()
    }

    private fun fetchUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            login.get()?.let {
                if (login.get()!!.length > 1) {
                    when (val response = repository.getUserList(login.get()!!)) {
                        is ApiResponse.Success -> {
                            _getUsers.postValue(mutableListOf<String>().apply {
                                for (item: Item in response.body!!.items!!)
                                    this.add(item.login!!)
                            })
                        }
                        is ApiResponse.Failure -> {
                            _message.postValue(response.message)
                        }
                    }
                }
            }
        }
    }

    fun onSelectServer(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        val lastPrefServer = repository.getPrefServer()
        val server = when ((view as AppCompatTextView).text) {
            "preprod" -> SCAN_PVZ_SERVER_PRED_PROD
            "qa" -> SCAN_PVZ_SERVER_QA
            "qa2" -> SCAN_PVZ_SERVER_QA2
            "dev" -> SCAN_PVZ_SERVER_DEV
            "dev2" -> SCAN_PVZ_SERVER_DEV2
            "dev_local" -> SCAN_PVZ_SERVER_DEV_LOCAL
            else -> SCAN_PVZ_SERVER_BASE
        }
        repository.setPrefServer(server)
//        if changed server -> fetchUsers
        if (server != lastPrefServer) {
            fetchUsers()
        }
    }

    fun login() {
        if (!login.get().isNullOrEmpty() && !password.get().isNullOrEmpty()) {
            val auth = AuthRequest(login.get()!!, password.get()!!)
            CoroutineScope(Dispatchers.IO).launch {
                when (val response = repository.logIn(auth)) {
                    is ApiResponse.Success -> {
                        onLogInResponse(response.body!!)
                    }
                    is ApiResponse.Failure -> {
                        _message.postValue(response.message)
                    }
                }
            }
        }
    }

    //    save auth param and go to baseActivity
    private fun onLogInResponse(auth: AuthResponce) {
        repository.setPrefLogin(auth.login)
        repository.setPrefUserId(auth.userId)
        repository.setPrefToken(auth.token)
        repository.setPrefLang(LOCALE_LANG)
        _navigateToBase.postValue(true)
    }


    override fun onCleared() {
        super.onCleared()
    }

}