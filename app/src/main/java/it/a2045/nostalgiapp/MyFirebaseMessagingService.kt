package it.a2045.nostalgiapp

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        Log.d(TAG, "new message received: ${p0.toString()}")
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Log.d(TAG, "new token: ${p0.toString()}")
    }

    companion object {
        val TAG = MyFirebaseMessagingService::class.java.canonicalName
    }
}
