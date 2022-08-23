package com.example.pushnotificationtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import okhttp3.Response
import org.w3c.dom.Text
import retrofit2.Retrofit
import java.lang.Exception

const val TOPIC = "/topics/myTopic"

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val title = findViewById<TextView>(R.id.title)
        val message = findViewById<TextView>(R.id.message)
        val btnSend = findViewById<Button>(R.id.btnSend)

        btnSend.setOnClickListener{
            if (title.text.toString().isNotEmpty() && message.text.toString().isNotEmpty()) {
                PushNotification(NotificationData(title.text.toString(), message.text.toString()), TOPIC)
                    .also {
                        sendNotification(it)
                    }
            }
        }
    }

    private fun sendNotification(notification: PushNotification) {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch (e : Exception) {
            Log.e(TAG, e.toString())
        }
    }
}