package com.android.chatsapp.notification

import android.R
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.legacy.content.WakefulBroadcastReceiver

class FirebaseDataReceiver : WakefulBroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Log.d("BroadcastReceiver::", "BroadcastReceiver")
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_menu_save)
            .setContentTitle(intent.extras!!.getString("title"))
            .setContentText(intent.extras!!.getString("message"))
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
    }
}