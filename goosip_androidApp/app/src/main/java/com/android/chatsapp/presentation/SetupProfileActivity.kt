package com.android.chatsapp.presentation

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.android.chatsapp.R
import com.android.chatsapp.api.Retrofit
import com.android.chatsapp.api.UserApiService
import com.android.chatsapp.databinding.ActivitySetupProfileBinding
import com.android.chatsapp.fragments.ChatsFragment
import com.android.chatsapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
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
import kotlin.coroutines.resume

class SetupProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupProfileBinding
    private var baseUrl = "http://192.168.125.10:8080/"
    private lateinit var user: User
    private var CurrentUser: String = FirebaseAuth.getInstance().uid.toString()
    private lateinit var path: String
    private lateinit var bitmap: Bitmap
    private val retrofit = Retrofit.createRetrofitInstance(baseUrl)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //back button
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //get user
        getUser(CurrentUser)

        binding.ProfilePicture.setOnClickListener(View.OnClickListener {
//            if (ContextCompat.checkSelfPermission(
//                    this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
                Intent(Intent.ACTION_PICK).also {
                    it.type = "image/*"
                    val minTypes = arrayOf("image/jpeg", "image/png")
                    it.putExtra(Intent.EXTRA_MIME_TYPES, minTypes)
                    startActivityForResult(it, 10)
                }
//            } else {
//                ActivityCompat.requestPermissions(
//                    this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
//                )
//            }
        })

        binding.btnUpdateProfile.setOnClickListener(View.OnClickListener {
            lifecycleScope.launch {
                val profilePicUrl = uploadImg()
                Log.i("result", profilePicUrl.toString())
                if (profilePicUrl != null) {
                    user = User(
                        uid =FirebaseAuth.getInstance().currentUser?.uid.toString(),
                        name = binding.username.text.toString(),
                        about = binding.about.text.toString(),
                        profileImg = profilePicUrl.toString(),
                        active ="1",
                        status = "online"
                    )
                    var result = saveUser(user)
                    if (result != null) {
                        Toast.makeText(
                            applicationContext,
                            "Your profile is created sucessfully!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@SetupProfileActivity, MainActivity::class.java)
                        startActivity(intent)

                    } else {
                        Log.i("UserSaved", result.toString())
                        Toast.makeText(
                            applicationContext, "Something went wrong!!", Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.i("ProfileImg", profilePicUrl.toString())
                    Toast.makeText(
                        applicationContext, "Something went wrong!!", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun getUser(currentUser:String) {
        try {
            val retrofit = Retrofit.createRetrofitInstance(baseUrl)
            val apiService = retrofit.create(UserApiService::class.java)
            val call = apiService.getUser(currentUser)
            call.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>, response: Response<User>
                ) {
                    if (response.isSuccessful) {
                        var result: User =response.body()!!
                        if (response.code() == 200) {
                            binding.username.setText(result.name)
                            binding.about.setText(result.about)
                            Picasso
                                .get()
                                .load(result.profileImg)
                                .into(binding.ProfilePicture)
                            Log.i("success", "added>>>>>>>>>>>>>" + result.toString())
                            Log.i("response body", response.toString())
                        }
                        Log.i("success", "added>>>>>>>>>>>>>" + result.toString())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 10) && (resultCode == Activity.RESULT_OK)) {
            val uri: Uri? = data?.data
            binding.ProfilePicture.setImageURI(uri)
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            binding.ProfilePicture.setImageBitmap(bitmap)
        }
    }

    private suspend fun saveUser(user: User): String? =
        suspendCancellableCoroutine { continuation ->
            try {
                val retrofit = Retrofit.createRetrofitInstance(baseUrl)
                val apiService = retrofit.create(UserApiService::class.java)
                val call = apiService.addUser(user)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>, response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            var result = response.body()
                            if (response.code() == 200) {
                                continuation.resume("saved User")
                            }
                            Log.i("success", "added>>>>>>>>>>>>>" + result.toString())
                            Log.i("response body", response.toString())
                        } else {
                            Log.i("response body", response.body().toString())
                            Log.i("error", response.message())
                            continuation.resume(null)
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.i("error", t.toString())
                        continuation.resume(null)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                continuation.resume(null)
            }
        }

    suspend fun uploadImg(): String? = suspendCancellableCoroutine { continuation ->
        try {
            val outputStream = ByteArrayOutputStream()
            if (bitmap != null && !bitmap!!.isRecycled) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val bytes: ByteArray = outputStream.toByteArray()
                val apiService = retrofit.create(UserApiService::class.java)
                val userId = RequestBody.create("text/plain".toMediaTypeOrNull(),FirebaseAuth.getInstance().currentUser?.uid.toString())
                val file = MultipartBody.Part.createFormData(
                    "file", "image.jpg", RequestBody.create("image/*".toMediaTypeOrNull(), bytes)
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}
