package com.android.chatsapp.notify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.android.chatsapp.databinding.ActivityTestNoteBinding
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject

class testNote : AppCompatActivity() {

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAy5GovvM:APA91bFNHPrm2IjDM0zv5Zvn9PStPfAnIjp58w1kGnaIHFlVmxQft82T85q6CoVNqqy9eUQLKfdXXTvC6W7E7_4moGwlC_r56zTCqCB6F-cZ_sSKoI3neLgZ4kWbiaTdtZB4QfpmLtPK"
    private val contentType = "application/json"
    private lateinit var binding: ActivityTestNoteBinding

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/Enter_your_topic_name")

        binding.submit.setOnClickListener {
            if (!TextUtils.isEmpty(binding.msg.text)) {
                val topic = "/topics/Enter_your_topic_name" //topic has to match what the receiver subscribed to

                val notification = JSONObject()
                val notifcationBody = JSONObject()

                try {
                    notifcationBody.put("title", "Enter_title")
                    notifcationBody.put("message", binding.msg.text)   //Enter your notification message
                    notification.put("to", topic)
                    notification.put("data", notifcationBody)
                    Log.e("TAG", "try")
                } catch (e: JSONException) {
                    Log.e("TAG", "onCreate: " + e.message)
                }

                sendNotification(notification)
            }
        }
    }

    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener { response ->
                Log.i("TAG", "onResponse: $response")
                binding.msg.setText("")
            },
            Response.ErrorListener {
                Toast.makeText(this@testNote, "Request error", Toast.LENGTH_LONG).show()
                Log.i("TAG", "onErrorResponse: Didn't work")
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }
}