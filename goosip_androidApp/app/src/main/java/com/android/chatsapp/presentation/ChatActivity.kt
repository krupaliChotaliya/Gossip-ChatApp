package com.android.chatsapp.presentation

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.chatsapp.R
import com.android.chatsapp.adapters.MessageAdapter
import com.android.chatsapp.api.MessageApiService
import com.android.chatsapp.api.Retrofit
import com.android.chatsapp.api.UserApiService
import com.android.chatsapp.databinding.ActivityChatBinding
import com.android.chatsapp.model.Message
import com.android.chatsapp.model.User
import com.android.chatsapp.viewmodel.UserViewModel
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.coroutines.resume
import kotlin.random.Random


class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: MessageAdapter
    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String
    private var senderUid: String = FirebaseAuth.getInstance().uid.toString()
    private var baseUrl = "http://192.168.205.10:8080/"
    private lateinit var bitmap: Bitmap
    private val retrofit = Retrofit.createRetrofitInstance(baseUrl)
    private lateinit var loadingbar: ProgressDialog
    private var date: Date = Date()
    private var userId = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var receiver: User
    private val itemViewModel: UserViewModel by viewModels()
    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("users")
    private lateinit var messages: ArrayList<Message>


    //TODO: FCM
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAy5GovvM:APA91bFNHPrm2IjDM0zv5Zvn9PStPfAnIjp58w1kGnaIHFlVmxQft82T85q6CoVNqqy9eUQLKfdXXTvC6W7E7_4moGwlC_r56zTCqCB6F-cZ_sSKoI3neLgZ4kWbiaTdtZB4QfpmLtPK"
    private val contentType = "application/json"

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            binding = ActivityChatBinding.inflate(layoutInflater)
            setContentView(binding.root)
            FirebaseApp.initializeApp(this)
            //fcm
            FirebaseMessaging.getInstance().subscribeToTopic("/topics/Enter_your_topic_name")


            //binding viewmodel for status
            binding.user = itemViewModel
            binding.lifecycleOwner = this

            val layoutManager = LinearLayoutManager(applicationContext)
            binding.messageRecycleView.layoutManager = layoutManager

            receiver = intent.getParcelableExtra("user") ?: return
            if (receiver != null) {
                Log.i("user", receiver.name)
                binding.username.text = receiver.name

                //to get real time data changes for status update
                collection.document(receiver.uid)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.i("error>>>", e.toString())
                            return@addSnapshotListener
                        }
                        if (snapshot != null && snapshot.exists()) {
                            Log.i("change", snapshot.data.toString())
                            val user = snapshot.toObject(User::class.java)
                            if (user != null) {
                                itemViewModel.updateValues(user.status)
                            }
                        }
                    }
                getRecevierStatus()
                updateStatus("status", "online")

                Picasso.get().load(receiver.profileImg).into(binding.profileImg)
                //for back button
                setSupportActionBar(findViewById(R.id.toolbar))
                supportActionBar?.setDisplayHomeAsUpEnabled(true)

                val receiverUid = receiver.uid
                senderRoom = senderUid + receiverUid
                receiverRoom = receiverUid + senderUid
                getMessages(senderRoom)


                //update message adapter
                val messageListRef =
                    firestore.collection("chats").document(senderRoom).collection("messageList")
                messageListRef.addSnapshotListener { querySnapshot, _ ->
                    if (querySnapshot != null) {
                        if (!querySnapshot.isEmpty) {
                            for (documentChange in querySnapshot.documentChanges) {
                                when (documentChange.type) {
                                    com.google.firebase.firestore.DocumentChange.Type.ADDED -> {
                                        val newDocument = documentChange.document.data
                                        println("New document added: $newDocument")
                                        getMessages(senderRoom)
                                    }

                                    com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> {
                                        val modifiedDocument = documentChange.document.data
                                        println("Document modified: $modifiedDocument")
                                    }

                                    com.google.firebase.firestore.DocumentChange.Type.REMOVED -> {
                                        val removedDocument = documentChange.document.data
                                        println("Document removed: $removedDocument")
                                    }
                                }
                            }
                        } else {
                            Log.i("ChatActivity", "messageList is empty")
                            return@addSnapshotListener
                        }
                    } else {
                        Log.i("ChatActivity", "messageList is not available")
                        return@addSnapshotListener
                    }
                }


                binding.messageBox.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        if (s.toString().trim().isEmpty()) {
                            updateStatus("status", "online")
                        } else {
                            updateStatus("status", "Typing")
                        }
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (s.toString().trim().isEmpty()) {
                            updateStatus("status", "online")
                        } else {
                            updateStatus("status", "Typing")
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        if (s.toString().trim().isEmpty()) {
                            updateStatus("status", "online")
                        } else {
                            updateStatus("status", "Typing")
                        }
                    }
                })


                binding.sendBtn.setOnClickListener(View.OnClickListener {
                    updateStatus("status", "typing")
                    val messagetxt: String = binding.messageBox.text.toString()
                    Log.i("messageBox", messagetxt)
                    if(messagetxt.trim() == ""){
                        Toast.makeText(this@ChatActivity, "Please, Enter Message", Toast.LENGTH_LONG).show()
                    }else{
                        val message =
                            Message(messagetxt, senderUid, 0, System.currentTimeMillis(), "text")
                        //api calling
                        saveMessage(message)
                    }

                    //fcm
                    if (!TextUtils.isEmpty(messagetxt)) {
                        val topic =
                            "/topics/Enter_your_topic_name" //topic has to match what the receiver subscribed to

                        val notification = JSONObject()
                        val notifcationBody = JSONObject()

                        try {
                            notifcationBody.put("title", "gossip")
                            notifcationBody.put(
                                "message",
                                messagetxt
                            )   //Enter your notification message
                            notification.put("to", topic)
                            notification.put("data", notifcationBody)
                            Log.e("TAG", "try")
                        } catch (e: JSONException) {
                            Log.e("TAG", "onCreate: " + e.message)
                        }
                        sendNotification(notification)
                    }
                })
                binding.attachbtn.setOnClickListener(View.OnClickListener {
                    updateStatus("status", "typing")
                    val intent = Intent()
                    intent.action = Intent.ACTION_GET_CONTENT
                    intent.type = "image/*"
                    startActivityForResult(Intent.createChooser((intent), "Select Image"), 10)
                })
            }
        } catch (e: Exception) {
            Log.i("OnCreate", e.message.toString())
        }
    }

    private fun getMessages(senderUid: String) {
        try {
            val retrofit = Retrofit.createRetrofitInstance(baseUrl)
            val apiService = retrofit.create(MessageApiService::class.java)
            val call: Call<ArrayList<Message>> =
                apiService.getMessages(senderUid)
            call.enqueue(object : Callback<ArrayList<Message>> {
                override fun onResponse(
                    call: Call<ArrayList<Message>>, response: Response<ArrayList<Message>>
                ) {
                    if (response.isSuccessful) {
                        messages = response.body()!!
                        if (response.code() == 200 && messages != null && messages.size != 0) {
                            Log.i("200[getMessages]", "added>>>>>>>>>>>>>$messages")

                            //update last message to user table
                            val last_message = messages[messages.size - 1].message
                            Log.i("lastMessage", last_message)
                            updateStatus("lastMessage", last_message)

                            adapter = MessageAdapter(applicationContext, messages)
                            binding.messageRecycleView.adapter = adapter
                            binding.messageRecycleView.scrollToPosition(messages.size - 1)
                        }
                        Log.i("success[getMessages]", "added>>>>>>>>>>>>>$messages")
                        Log.i("response body[getMessages]", response.toString())
                    } else {
                        Log.i("response body[getMessages]", response.body().toString())
                        Log.i("error[getMessages]", response.message())
                    }
                }

                override fun onFailure(call: Call<ArrayList<Message>>, t: Throwable) {
                    Log.i("error", t.toString())
                }
            })
        } catch (e: Exception) {
            Log.i("[getMessages] ", e.message.toString())
        }
    }

    private fun saveMessage(message: Message) {
        try {
            val retrofit = Retrofit.createRetrofitInstance(baseUrl)
            val apiService = retrofit.create(MessageApiService::class.java)
            val call: Call<ResponseBody> =
                apiService.saveMessage(message, senderRoom, receiverRoom)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>, response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            Log.i("200", "added>>>>>>>>>>>>>$messages")
                            binding.messageBox.setText("")
                            binding.messageRecycleView.scrollToPosition(messages.size - 1)
                            getMessages(senderRoom)
                        }
                        Log.i("success", "added>>>>>>>>>>>>>$messages")
                        Log.i("response body", response.toString())
                    } else {
                        Log.i("response body", response.body().toString())
                        Log.i("error", response.message())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("error", t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == RESULT_OK && data != null && data.data != null) {
            //loading bar= progressbar
            loadingbar = ProgressDialog(this)
            loadingbar.setMessage("Please wait,image is sending... ")
            loadingbar.show()
            val uri: Uri? = data.data
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            lifecycleScope.launch {
                val profilePicUrl = uploadImg()
                Log.i("result", profilePicUrl.toString())
                if (profilePicUrl != null) {
                    Log.i("uploaded", "success")
                    val message =
                        Message(profilePicUrl.toString(), senderUid, 0, date.time, "file")
                    saveMessage(message)
                } else {
                    Log.i("error", "not uploaded")
                }
            }
        }
    }

    private suspend fun uploadImg(): String? = suspendCancellableCoroutine { continuation ->
        try {
            val randomPattern = generateRandomPattern(10)
            val outputStream = ByteArrayOutputStream()
            if (!bitmap.isRecycled) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val bytes: ByteArray = outputStream.toByteArray()
                val apiService = retrofit.create(UserApiService::class.java)
                val userId = RequestBody.create("text/plain".toMediaTypeOrNull(), randomPattern)
                val file = MultipartBody.Part.createFormData(
                    "file",
                    "image.jpg",
                    RequestBody.create("image/*".toMediaTypeOrNull(), bytes)
                )
                val call: Call<ResponseBody> = apiService.uploadImg(file, userId)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>, response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()?.string()
                            Log.i("Success>>>", responseBody ?: "")
                            continuation.resume(responseBody)
                            loadingbar.dismiss()
                        } else {
                            val errorBody = response.errorBody()?.string()
                            val errorCode = response.code()
                            Log.e("Error", "Code: $errorCode, Body: $errorBody")
                            continuation.resume(null)
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.i("[onFailure]error>>>>>>>>>>>", t.message.toString())
                        continuation.resume(null)
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
            continuation.resume(null)
        }
    }

    fun updateStatus(fieldName: String, value: String) {
        try {
            val retrofit = Retrofit.createRetrofitInstance(baseUrl)
            val apiService = retrofit.create(UserApiService::class.java)
            val call: Call<ResponseBody> =
                apiService.updateStatus(userId, fieldName, value)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>, response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.string()
                        Log.i("Success>>>", responseBody ?: "")
                        getRecevierStatus()
                        Log.i("success[updateStatus]", responseBody.toString())
                        Log.i("response body", response.toString())
                    } else {
                        Log.i("response body", response.body().toString())
                        Log.i("error", response.message())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("error", t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getRecevierStatus() {
        try {
            val retrofit = Retrofit.createRetrofitInstance(baseUrl)
            val apiService = retrofit.create(UserApiService::class.java)
            val call: Call<User> =
                apiService.getUser(receiver.uid)
            call.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>, response: Response<User>
                ) {
                    if (response.isSuccessful) {
                        val result: User = response.body()!!
                        if (response.code() == 200) {
                            Log.i("resultStatus", result.status)
                            itemViewModel.updateValues(result.status)
                        }
                        Log.i("success[getRecevierStatus]", result.toString())
                        Log.i("response body", response.toString())
                    } else {
                        Log.i("response body", response.body().toString())
                        Log.i("error", response.message())
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.i("error", t.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateRandomPattern(length: Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random
        return (1..length)
            .map { characters[random.nextInt(characters.length)] }
            .joinToString("")
    }


    override fun onResume() {
        super.onResume()
        //TODO:last msg
//        val lastMessage = messages.last()
//        Log.i("lastmsg",lastMessage.message)
        getRecevierStatus()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): String? {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return currentTime.format(formatter)
    }

    //back btn
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSupportNavigateUp(): Boolean {
        updateStatus("status", "last seen " + getCurrentTime())
        finish()
        return super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBackPressed() {
        updateStatus("status", "last seen " + getCurrentTime())
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        updateStatus("status", "last seen " + getCurrentTime())
        super.onPause()
    }

    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            com.android.volley.Response.Listener { response ->
                Log.i("TAG", "onResponse: $response")
                binding.messageBox.setText("")
            },
            com.android.volley.Response.ErrorListener {
                Toast.makeText(this@ChatActivity, "Request error", Toast.LENGTH_LONG).show()
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