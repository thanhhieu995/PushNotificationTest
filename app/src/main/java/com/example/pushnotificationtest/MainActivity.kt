package com.example.pushnotificationtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import io.reactivex.annotations.SchedulerSupport.IO
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.Response
import org.w3c.dom.Text
import retrofit2.Retrofit
import java.lang.Exception
import kotlin.coroutines.coroutineContext

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
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler{
            _, throwable ->
        throwable.printStackTrace()
    }

     private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch{
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful) {
                val responseJson: String? = Gson().toJson(response)
//                Log.d(TAG, "Response: ${Gson().toJson(response)}")
                Log.d(TAG, "Response: $responseJson")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch (e : Exception) {
            Log.e(TAG, e.toString())
        }
    }
}