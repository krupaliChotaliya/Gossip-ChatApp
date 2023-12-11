package com.android.chatsapp.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Retrofit {

    companion object {

        fun createOkHttpClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(2, TimeUnit.MINUTES) // Set connect timeout
                .readTimeout(2, TimeUnit.MINUTES)    // Set read timeout
                .writeTimeout(2, TimeUnit.MINUTES)
                .build()
        }

        var gson = GsonBuilder().setLenient().create()

        fun createRetrofitInstance(baseUrl: String): Retrofit {
            val retrofit = Retrofit.Builder().baseUrl(baseUrl).client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
            return retrofit
        }
    }


}