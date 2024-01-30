package com.android.chatsapp.presentation

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.android.chatsapp.R
import com.android.chatsapp.api.Retrofit
import com.android.chatsapp.api.UserApiService
import com.android.chatsapp.databinding.ActivitySetupProfileBinding
import com.android.chatsapp.helper.Constants
import com.android.chatsapp.model.User
import com.github.dhaval2404.imagepicker.ImagePicker
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
    private var baseUrl = Constants.API_BASE_URL
    private lateinit var user: User
    private var CurrentUser: String = FirebaseAuth.getInstance().uid.toString()
    private lateinit var bitmap: Bitmap
    private val retrofit = Retrofit.createRetrofitInstance(baseUrl)
    private lateinit var loadingbar: ProgressDialog
    private var selectedImageUri: Uri? = null

    private val CAMERA_PERMISSION_CODE = 1001
    private val APP_SETTINGS_REQUEST_CODE = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.cameraIcon.setOnClickListener{
            checkCameraPermission()
        }
        //back button
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //get user
        getUser(CurrentUser)

        binding.btnUpdateProfile.setOnClickListener(View.OnClickListener {
            lifecycleScope.launch {
                loadingbar = ProgressDialog(this@SetupProfileActivity)
                loadingbar.setMessage("Please wait,Your Profile is updating..")
                loadingbar.show()

                var uid: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
                var name: String = binding.username.text.toString()
                var about: String = binding.about.text.toString()

                if (uid == null || name.equals("null") || name.isEmpty() || about.equals("null") || about.isEmpty()) {
                    Toast.makeText(
                        applicationContext, "Please Fill above details.", Toast.LENGTH_SHORT
                    ).show()
                    loadingbar.dismiss()
                } else {
                    if (selectedImageUri != null) {
                        // If an image is selected, perform image-related tasks
                        println("Image URI: $selectedImageUri")
                        val profilePicUrl = uploadImg()
                        Log.i("result", profilePicUrl.toString())
                        if (profilePicUrl != null) {
                            user = User(
                                uid = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                                name = binding.username.text.toString(),
                                about = binding.about.text.toString(),
                                profileImg = profilePicUrl.toString(),
                                active = "1",
                                status = "online",
                            )
                            var result = saveUser(user)
                            if (result != null) {
                                loadingbar.dismiss()
                                Toast.makeText(
                                    applicationContext,
                                    "Your profile is created successfully!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(this@SetupProfileActivity, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                loadingbar.dismiss()
                                Log.i("UserSaved", result.toString())
                                Toast.makeText(
                                    applicationContext, "Something went wrong!!", Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            loadingbar.dismiss()
                            Toast.makeText(
                                applicationContext, "Something went wrong!!", Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Log.i("Setup", "image not selected!!")
                        var uid: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        var name: String = binding.username.text.toString()
                        var about: String = binding.about.text.toString()
                        updateUsernameAndAbout(uid, name, about)
                        loadingbar.dismiss()
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == RESULT_OK && data != null && data.data != null) {
            Log.i("onActivityResult","Image is selected")
            selectedImageUri = data?.data
            if (selectedImageUri!=null){
                binding.ProfilePicture.setImageURI(selectedImageUri)
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                binding.ProfilePicture.setImageBitmap(bitmap)
            }
        }
    }
    
    private fun getUser(currentUser: String) {
        try {
            val retrofit = Retrofit.createRetrofitInstance(baseUrl)
            val apiService = retrofit.create(UserApiService::class.java)
            val call = apiService.getUser(currentUser)
            call.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>, response: Response<User>
                ) {
                    if (response.isSuccessful) {
                        var result: User = response.body()!!
                        if (response.code() == 200) {
                            binding.username.setText(result.name)
                            binding.about.setText(result.about)
                            Picasso.get().load(result.profileImg).into(binding.ProfilePicture)
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
                val userId = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    FirebaseAuth.getInstance().currentUser?.uid.toString()
                )
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

    private suspend fun updateUsernameAndAbout(uid: String, name: String, about: String) {
        suspendCancellableCoroutine { continuation ->
            try {
                val retrofit = Retrofit.createRetrofitInstance(baseUrl)
                val apiService = retrofit.create(UserApiService::class.java)
                val call = apiService.updateUsernameAndAbout(uid, name, about)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>, response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            var result = response.body()
                            if (response.code() == 200) {
                                continuation.resume("Partial updated User")
                                Toast.makeText(
                                    applicationContext,
                                    "Your profile is updated successfully!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(this@SetupProfileActivity, MainActivity::class.java)
                                startActivity(intent)
                            }
                            Log.i("success", "Partial updated User" + result.toString())
                            Log.i("response body", response.toString())
                        } else {
                            Log.i("response body", response.body().toString())
                            Log.i("error", response.message())
                            continuation.resume(null)
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(
                            applicationContext, "Something went wrong!!", Toast.LENGTH_SHORT
                        ).show()
                        Log.i("error", t.toString())
                        continuation.resume(null)
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                continuation.resume(null)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
            // Proceed with initializing your camera
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(10)
        } else {
            // Request camera permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("setup Activity","permission granted")
                    // Permission granted
                    // Proceed with initializing your camera
                    ImagePicker.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(10)
                } else {
                    Log.i("setup Activity","permission denied")

                    if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        // Explain to the user why the permission is needed and redirect to the app settings
                        showPermissionExplanationDialog()
                    } else {
                        // Inform the user or handle this case accordingly
//                        Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    private fun showPermissionExplanationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Camera Permission Required")
            .setMessage("This app requires camera permission to function properly. Please grant the permission in the app settings.")
            .setPositiveButton("Go to Settings") { dialog, which ->
                navigateToAppSettings()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                // Handle the cancel button click if needed
            }
            .show()
    }

    // Open the app settings
    private fun navigateToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, APP_SETTINGS_REQUEST_CODE)
    }

}