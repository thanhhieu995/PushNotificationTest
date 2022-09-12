package com.example.pushnotificationtest

import android.content.Context
import android.os.StrictMode
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.okhttp.Call
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import org.json.JSONObject
import java.io.IOException


class FcmSend {
    val BASE_URL = "https://fcm.googleapis.com/fcm/send"
    val SERVER_KEY = "AAAAZJaHDns:APA91bFQ5Zs07I5WeSAK6ERt_XX3LVjYAXCKwnaNfGWSkVdT0Cutg1Z1xfvMCZswcBzFueYpgy1p-49NbH32qAM886boMVia6zCZ8LHmYrohBmWWL-vDl51GBA7d6jqFgNnS5sNeTuSV"

    fun notificationSend(context: Context, token: String, title: String, message: String) {
        val  policy : StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val queue: RequestQueue = Volley.newRequestQueue(context)

        try {
            val jsonObject: JSONObject = JSONObject()
            jsonObject.put("to", token)
            val notification: JSONObject = JSONObject()
            notification.put("title", title)
            notification.put("body", message)
            jsonObject.put("notification", notification)

//            val jsonObjectRequest = JsonObjectRequest(BASE_URL, jsonObject, {
//                return@JsonObjectRequest println("success jsonObject$jsonObject")
//            }, {})

            val jsonObjReq = JsonObjectRequest(
                Request.Method.POST,
                BASE_URL, null,
                { response ->
                    response.put("Content-value", "application/json")
                    response.put("Authorization", SERVER_KEY)
                    Log.d("RESULT", response.toString())
                }) { error ->
                VolleyLog.d("RESULT", "Error: " + error.message)
            }

            Volley.newRequestQueue(context).add(jsonObjReq)


//            queue.add(jsonObjectRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}