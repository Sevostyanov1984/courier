package com.cdek.courier.delayedProcesses

import com.cdek.courier.data.models.firebase.FcmRequest
import com.cdek.courier.utils.FCM_KEY_GET_TASK
import com.cdek.courier.utils.FCM_KEY_UPDATE_COURIER
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var repository: DelayedProcessesRepository

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

//        Log.d("myFirebase", "onMessageReceived")
        val data = remoteMessage.data

        val message = data["message"]
        if (message != null) {
            when (message) {
//                "sendFiles" -> getPathListForSend()
                FCM_KEY_GET_TASK -> {
                    // run get fragment_task
                }
                FCM_KEY_UPDATE_COURIER -> {
                    // clear user_token & go to auth
                }
            }
        }
    }


    override fun onNewToken(newToken: String) {
        val oldToken = repository.getPrefFCMToken()
        repository.setPrefFCMToken(newToken)
        if (oldToken != "") {
            val fcmRequest =
                FcmRequest(repository.getPrefUserId(), oldToken, newToken)
            CoroutineScope(Dispatchers.IO).launch {
                repository.sendFCMToken(fcmRequest)
            }
        }
    }

}