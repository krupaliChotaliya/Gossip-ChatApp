package com.android.chatsapp.presentation

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
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
import com.android.chatsapp.notification.FirebaseService
import com.android.chatsapp.notification.entity.NotificationData
import com.android.chatsapp.notification.entity.PushNotification
import com.android.chatsapp.notification.network.RetrofitInstance
import com.android.chatsapp.viewmodel.UserViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.coroutines.resume
import kotlin.random.Random

const val TOPIC = "/topics/myTopic"

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: MessageAdapter
    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String
    private var senderUid: String = FirebaseAuth.getInstance().uid.toString()
    private var baseUrl = "http://192.168.125.10:8080/"
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
    private lateinit var FCMTOKEN:String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)

        //FCM
        FirebaseService.sharedPreferences=getPreferences(Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (task.isSuccessful && task.result != null) {
                    FCMTOKEN=task.getResult().toString()
                    Log.i("token", task.result!!)
                    Log.i("FCMtoken", FCMTOKEN)
                }
            }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)


        //binding viewmodel
        binding.user = itemViewModel
        binding.lifecycleOwner = this

        val layoutManager = LinearLayoutManager(applicationContext)
        binding.messageRecycleView.layoutManager = layoutManager

        receiver = intent.getParcelableExtra("user")!!
        if (receiver != null) {
            Log.i("user", receiver.name)
            binding.username.text = receiver.name

            //to get real time data changes
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
            updateStatus("online")

            Picasso.get().load(receiver.profileImg).into(binding.profileImg)
            //for back button
            setSupportActionBar(findViewById(R.id.toolbar))
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            var receiverUid = receiver.uid
            senderRoom = senderUid + receiverUid
            receiverRoom = receiverUid + senderUid
            getMessages(senderRoom)


            //update message adapter
            val messageListRef =
                firestore.collection("chats").document(senderRoom).collection("messageList")
            messageListRef.addSnapshotListener { querySnapshot, _ ->
                if (querySnapshot != null && !querySnapshot.isEmpty) {
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
                        updateStatus("online")
                    } else {
                        updateStatus("Typing")
                    }
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (s.toString().trim().isEmpty()) {
                        updateStatus("online")
                    } else {
                        updateStatus("Typing")
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().trim().isEmpty()) {
                        updateStatus("online")
                    } else {
                        updateStatus("Typing")
                    }
                }
            })


            binding.sendBtn.setOnClickListener(View.OnClickListener {
                updateStatus("typing")
                var messagetxt: String = binding.messageBox.text.toString()
                Log.i("messageBox", messagetxt)

                var message: Message =
                    Message(messagetxt, senderUid, 0, System.currentTimeMillis(), "text")
                //api calling
                saveMessage(message)
                Toast.makeText(
                    this,
                    FCMTOKEN,
                    Toast.LENGTH_SHORT
                ).show()

                //FCM send notification
                if (messagetxt.isNotEmpty()) {
                    PushNotification(
                        FCMTOKEN,
                        NotificationData("gossip", messagetxt)
                    ).also {
                        sendNotification(it)
                    }
                }
            })
            binding.attachbtn.setOnClickListener(View.OnClickListener {
                updateStatus("typing")

                val intent: Intent = Intent()
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type = "image/*"
                startActivityForResult(Intent.createChooser((intent), "Select Image"), 10)
            })
        }
    }

    private fun getMessages(senderUid: String) {
        try {
            val retrofit = Retrofit.createRetrofitInstance(baseUrl)
            val apiService = retrofit.create(MessageApiService::class.java)
            var call: Call<ArrayList<Message>> =
                apiService.getMessages(senderUid)
            call.enqueue(object : Callback<ArrayList<Message>> {
                override fun onResponse(
                    call: Call<ArrayList<Message>>, response: Response<ArrayList<Message>>
                ) {
                    if (response.isSuccessful) {
                        messages = response.body()!!
                        if (response.code() == 200) {
                            Log.i("200[getMessages]", "added>>>>>>>>>>>>>" + messages)
                            adapter = MessageAdapter(applicationContext, messages)
                            binding.messageRecycleView.adapter = adapter
                            binding.messageRecycleView.scrollToPosition(messages.size - 1)
                        }
                        Log.i("success[getMessages]", "added>>>>>>>>>>>>>" + messages)
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
            var call: Call<ResponseBody> =
                apiService.saveMessage(message, senderRoom, receiverRoom)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>, response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            Log.i("200", "added>>>>>>>>>>>>>" + messages)
                            binding.messageBox.setText("")
                            binding.messageRecycleView.scrollToPosition(messages.size - 1)
                            getMessages(senderRoom)
                        }
                        Log.i("success", "added>>>>>>>>>>>>>" + messages)
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
        if (requestCode == 10 && resultCode == RESULT_OK && data != null && data!!.data != null) {
            //loading bar= progressbar
            loadingbar = ProgressDialog(this)
            loadingbar.setMessage("Please wait,image is sending... ")
            loadingbar.show()
            val uri: Uri? = data?.data
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            lifecycleScope.launch {
                val profilePicUrl = uploadImg()
                Log.i("result", profilePicUrl.toString())
                if (profilePicUrl != null) {
                    Log.i("uploaded", "success")
                    var message =
                        Message(profilePicUrl.toString(), senderUid, 0, date.time, "file")
                    saveMessage(message)
                } else {
                    Log.i("error", "not uploaded")
                }
            }
        }
    }

    suspend fun uploadImg(): String? = suspendCancellableCoroutine { continuation ->
        try {
            val randomPattern = generateRandomPattern(10)
            val outputStream = ByteArrayOutputStream()
            if (bitmap != null && !bitmap!!.isRecycled) {
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

    fun updateStatus(status: String) {
        try {
            val retrofit = Retrofit.createRetrofitInstance(baseUrl)
            val apiService = retrofit.create(UserApiService::class.java)
            var call: Call<ResponseBody> =
                apiService.updateStatus(userId, status)
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
            var call: Call<User> =
                apiService.getUser(receiver.uid)
            call.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>, response: Response<User>
                ) {
                    if (response.isSuccessful) {
                        var result: User = response.body()!!
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

    fun generateRandomPattern(length: Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random
        return (1..length)
            .map { characters[random.nextInt(characters.length)] }
            .joinToString("")
    }

    override fun onStart() {
        super.onStart()
        getRecevierStatus()
    }

    override fun onResume() {
        super.onResume()
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
        updateStatus("last seen " + getCurrentTime())
        finish()
        return super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBackPressed() {
        updateStatus("last seen " + getCurrentTime())
        super.onBackPressed()
    }

    //SEND NOTIFICATION
    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.i("notify[sendNotification]", notification.to)
                val response:Call<JsonElement> = RetrofitInstance.api.postNotification(notification)

                val retrofitResponse = response.execute()
                if (retrofitResponse.isSuccessful) {
                    val responseBody = retrofitResponse.body()
                    if (responseBody != null) {
                        val jsonResponse = responseBody.toString()
                        Log.i("Response[sendNotification]", jsonResponse)
                    } else {
                        Log.e("Response[sendNotification]", "Empty response body")
                    }
                } else {
                    val errorBody = retrofitResponse.errorBody()?.string()
                    Log.e("Error Response[sendNotification]", errorBody ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.e("Exception[sendNotification] ", e.message.toString())
            }
        }
}