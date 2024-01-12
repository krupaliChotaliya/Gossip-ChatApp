package com.android.chatsapp.api

import com.android.chatsapp.model.Message
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageApiService {

    @POST("/message")
    fun saveMessage(
        @Body message: Message,
        @Query("senderRoom") senderRoom: String,
        @Query("receiverRoom") receiverRoom: String
    ): Call<ResponseBody>

    @GET("/message")
    fun getMessages(@Query("senderRoom") senderRoom: String): Call<ArrayList<Message>>
}