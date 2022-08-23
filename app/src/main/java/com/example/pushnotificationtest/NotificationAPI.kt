package com.example.pushnotificationtest

import com.example.pushnotificationtest.Constants.Companion.CONTENT_TYPE
import com.example.pushnotificationtest.Constants.Companion.SERVER_KEY
import com.squareup.okhttp.Response
import com.squareup.okhttp.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type: $CONTENT_TYPE")
    @POST("console/send")
    fun postNotification(@Body notification: PushNotification) : retrofit2.Response<ResponseBody>
}