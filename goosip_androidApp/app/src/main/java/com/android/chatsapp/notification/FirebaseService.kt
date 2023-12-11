package com.android.chatsapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.android.chatsapp.R
import com.android.chatsapp.presentation.ChatActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

private const val CHANNEL_ID ="my_channel"

class FirebaseService:FirebaseMessagingService() {

    companion object {
        var sharedPreferences:SharedPreferences?= null
        var token:String?
            get() {
                return sharedPreferences?.getString("token","")
            }
            set(value) {
                sharedPreferences?.edit()?.putString("token",value)?.apply()
            }
    }

    override fun onNewToken(Newtoken: String) {
        Log.d("[onNewToken]", "Refreshed token: $token")
        token=Newtoken

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(
            this,
            ChatActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent,
            FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val notification=NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.avatar)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId=Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        notificationManager.notify(notificationId,notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){

        val channelName="channelName"
        val channel=NotificationChannel(CHANNEL_ID,channelName,IMPORTANCE_HIGH).apply {
            description="My channel description"
            enableLights(true)
            lightColor=Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }
}