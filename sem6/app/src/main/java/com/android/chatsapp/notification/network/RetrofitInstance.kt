package com.android.chatsapp.notification.network

import com.android.chatsapp.api.Retrofit
import com.android.chatsapp.notification.Constants

class RetrofitInstance {

    companion object{
        val api by lazy {
           var retrofit= Retrofit.createRetrofitInstance(Constants.BASE_URL)
            retrofit.create(NotificationApi::class.java)
        }
    }
}