package com.example.pushnotificationtest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


const val TOPIC = "/topics/myTopic"

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        val title = findViewById<EditText>(R.id.title)
        val message = findViewById<EditText>(R.id.message)
        val btnSend = findViewById<Button>(R.id.btnSend)

        btnSend.setOnClickListener{
            val edtTitle = title.text.toString()
            val edtMessage = message.text.toString()

            if (title.text.toString().isNotEmpty() && message.text.toString().isNotEmpty()) {
                PushNotification(NotificationData(edtTitle, edtMessage), TOPIC)
                    .also {
                        sendNotification(it)
                    }
            } else {
                Toast.makeText(this, "enter message", Toast.LENGTH_LONG).show()
            }
        }

         onTokenRefresh()
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler{
            _, throwable ->
        throwable.printStackTrace()
    }

     private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch{
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful) {
//                val responseJson: String? = Gson().toJson(response)
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
//                Log.d(TAG, "Response: $responseJson")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch (e : Exception) {
            Log.e(TAG, e.toString())
        }
    }

    fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: $refreshedToken")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(refreshedToken: String?) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val ref : DatabaseReference = database.getReference("server/saving-data/IDs")
        ref.push().setValue(refreshedToken)
    }
}