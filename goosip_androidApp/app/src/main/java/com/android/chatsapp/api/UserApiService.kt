package com.android.chatsapp.api

import com.android.chatsapp.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface UserApiService {

    @POST("/user")
    fun addUser(@Body user:User):Call<ResponseBody>

    @PUT("/user")
    fun updateUser(@Body user:User):Call<ResponseBody>

    @Multipart
    @POST("/upload")
    fun uploadImg(@Part file: MultipartBody.Part, @Part("userId") UserId: RequestBody):Call<ResponseBody>

    @GET("/users")
    fun getAllUsers(@Query("currentUser") currentUser: String):Call<ArrayList<User>>

    @GET("/user")
    fun getUser(@Query("currentUserId") currentUser: String):Call<User>

    @PUT("/userStatus")
    fun updateStatus(@Query("uid")uid:String,@Query("fieldName")fieldName:String,@Query("value")value:String):Call<ResponseBody>

    @PUT("/updateUser")
    fun updateUsernameAndAbout(@Query("uid")uid:String,@Query("name")name:String,@Query("about")about:String):Call<ResponseBody>
}