package com.android.chatsapp.api

import com.android.chatsapp.model.Story
import com.android.chatsapp.model.UserStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface StoryApiService {

    @Multipart
    @POST("/uploadStoryImg")
    fun uploadImg(@Part file: MultipartBody.Part, @Part("userId") UserId: RequestBody): Call<ResponseBody>

    @POST("/story")
    fun addStory(@Body story:Story):Call<ResponseBody>

    @GET("/story")
    fun getStories(@Query("currentUserId") currentUser: String):Call<Story>

    @GET("/stories")
    fun getAllStoriesofUser(@Query("currentUser") currentUser: String):Call<ArrayList<Story>>

    @GET("/getOnlyStory")
    fun getOnlyStory():Call<ArrayList<UserStory>>
}