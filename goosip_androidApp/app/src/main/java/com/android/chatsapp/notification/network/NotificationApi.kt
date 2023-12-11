package com.android.chatsapp.notification.network

import com.android.chatsapp.notification.Constants.Companion.CONTENT_TYPE
import com.android.chatsapp.notification.Constants.Companion.SERVER_KEY
import com.android.chatsapp.notification.entity.PushNotification
import com.google.gson.JsonElement
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization:key=$SERVER_KEY","Content-Type:$CONTENT_TYPE")
    @POST("/fcm/send")
    fun postNotification(
        @Body notification: PushNotification
    ): retrofit2.Call<JsonElement>
}