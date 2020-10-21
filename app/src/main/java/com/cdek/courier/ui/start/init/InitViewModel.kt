package com.cdek.courier.ui.start.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cdek.courier.data.models.auth.TokenResponse
import com.cdek.courier.data.models.firebase.FcmRequest
import com.cdek.courier.data.network.ApiResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class InitViewModel @Inject constructor(private val repository: InitRepository) : ViewModel() {

    //    Events
    private val _navigateToAuth = MutableLiveData<Boolean>()
    val navigateToAuth: LiveData<Boolean>
        get() = _navigateToAuth

    private val _navigateToBase = MutableLiveData<Boolean>()
    val navigateToBase: LiveData<Boolean>
        get() = _navigateToBase

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun navigateToAuthHandled() {
        _navigateToAuth.value = false
    }

    fun navigateToBaseHandled() {
        _navigateToBase.value = false
    }

    fun messageHandled() {
        _message.value = ""
    }


    fun checkDefaultServer() {
        if (repository.getPrefServer().isEmpty()) {
            repository.setPrefServer()
        }
    }


    fun permissionsGranted() {
        val token = repository.getPrefToken()
        val login = repository.getPrefLogin()
        if (token.isEmpty() || login.isEmpty()) {
            _navigateToAuth.value = true
        } else {
            onCheckToken(token)
        }
    }

    private fun onCheckToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = repository.checkToken(token)) {
                is ApiResponse.Success -> {
                    checkTokenResponse(response.body)
                }
                is ApiResponse.Failure -> {
                    _message.postValue(response.message)
//                    TODO replace
//                    _navigateToAuth.postValue(true)
                    _navigateToBase.postValue(true)
                }
            }
        }
    }

    private fun checkTokenResponse(tokenResponse: TokenResponse?) {
        val login = repository.getPrefLogin()
        if (tokenResponse?.login == login && !tokenResponse.blocked!!) {
            _navigateToBase.postValue(true)
//            onSendFCMToken()
        } else {
            _navigateToAuth.postValue(true)
        }
    }

    private fun onSendFCMToken() {
        val fcmRequest = FcmRequest(repository.getPrefUserId(), "", repository.getPrefFCMToken())
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = repository.sendFCMToken(fcmRequest)) {
                is ApiResponse.Success -> {
                    _navigateToBase.postValue(true)
                }
                is ApiResponse.Failure -> {
                    _message.postValue(response.message)
                    _navigateToAuth.postValue(true)
                }
            }
        }
    }

    fun getFCMToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
//                    Log.w(TAG, "getInstanceId failed", fragment_task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                _message.value = token
            })
    }


    override fun onCleared() {
        super.onCleared()
    }
}