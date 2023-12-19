package com.android.chatsapp.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.chatsapp.R
import com.android.chatsapp.presentation.ChatActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class testNotify : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i("onMessageReceived","enter____________________")
        sendNotification(remoteMessage.data["title"], remoteMessage.data["body"])
    }

    private fun sendNotification(messageTitle: String?, messageBody: String?) {
        Log.i("sendNotification","enter____________________")
        val context: Context = applicationContext // Initialize the context

        val intent = Intent(context, ChatActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0 /* request code */,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val pattern = longArrayOf(500, 500, 500, 500, 500)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, "channelId") // Provide a channel ID
            .setSmallIcon(R.drawable.avatar)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setVibrate(pattern)
            .setLights(Color.BLUE, 1, 1)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}
