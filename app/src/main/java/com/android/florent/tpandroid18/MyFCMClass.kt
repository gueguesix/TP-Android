package com.android.florent.tpandroid18

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFCMClass : FirebaseMessagingService() {

    private val TAG = "JSA-FCM"
    private lateinit var database : DatabaseReference

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        if (remoteMessage!!.notification != null) {

        }

        if (remoteMessage.data.isNotEmpty()) {
            Log.e(TAG, "Data: " + remoteMessage.data)
        }
    }

    override fun onNewToken(token: String?) {
        database = FirebaseDatabase.getInstance().reference
        database.child("fcmToken").setValue(token).addOnSuccessListener {
            Log.d("token", "updated token in db: $token")
        }
                .addOnFailureListener {
                    Log.e("token", "refused refresh: $it")
                }
    }
}