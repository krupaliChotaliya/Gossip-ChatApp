package com.android.chatsapp.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.chatsapp.adapters.StoryAdapter
import com.android.chatsapp.api.Retrofit
import com.android.chatsapp.api.StoryApiService
import com.android.chatsapp.databinding.FragmentStatusBinding
import com.android.chatsapp.helper.Constants
import com.android.chatsapp.model.Story
import com.android.chatsapp.model.User
import com.android.chatsapp.model.UserStory
import com.android.chatsapp.presentation.StoryViewActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
import java.util.Date
import kotlin.coroutines.resume

class StatusFragment : Fragment(), StoryAdapter.RecycleViewEvent {

    private lateinit var binding: FragmentStatusBinding
    private var baseUrl = Constants.API_BASE_URL
    private var CurrentUser: String = FirebaseAuth.getInstance().uid.toString()
    private lateinit var userStatus: ArrayList<User>
    private var selectedImageUri: Uri? = null
    private lateinit var bitmap: Bitmap
    private val retrofit = Retrofit.createRetrofitInstance(baseUrl)
    private lateinit var story: Story
    private var date: Date = Date()
    private lateinit var stories: ArrayList<UserStory>
    private val firestore = FirebaseFirestore.getInstance()
    private val APP_SETTINGS_REQUEST_CODE = 123
    private val CAMERA_PERMISSION_REQUEST_CODE = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val usersCollectionRef = firestore.collection("users")

        usersCollectionRef.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                // Handle the error
                Log.e("ChatActivity", "Error getting users collection updates: ${error.message}")
                return@addSnapshotListener
            }

            if (querySnapshot != null) {
                for (documentChange in querySnapshot.documentChanges) {
                    val userId = documentChange.document.id
                    val userDocumentRef = usersCollectionRef.document(userId)

                    // Listen for changes in the user document itself
                    userDocumentRef.addSnapshotListener { userDocumentSnapshot, userDocumentError ->
                        if (userDocumentError != null) {
                            // Handle the error
                            Log.e("ChatActivity", "Error getting user document updates: ${userDocumentError.message}")
                            return@addSnapshotListener
                        }

                        if (userDocumentSnapshot != null && userDocumentSnapshot.exists()) {
                            val userData = userDocumentSnapshot.data
                            println("User document updated: $userData")
                            getOnlyStory()
                            Log.i("User updated","document changes")
                            // Handle the updated user document as needed
                        }
                    }

                    // Listen for changes in the subcollection "storyList" of each user
                    val storyListRef = userDocumentRef.collection("storyList")
                    storyListRef.addSnapshotListener { storyListSnapshot, storyListError ->
                        if (storyListError != null) {
                            // Handle the error
                            Log.e("ChatActivity", "Error getting storyList updates: ${storyListError.message}")
                            return@addSnapshotListener
                        }

                        if (storyListSnapshot != null) {
                            for (StoryDocumentChange in storyListSnapshot.documentChanges) {
                                when (StoryDocumentChange.type) {
                                    com.google.firebase.firestore.DocumentChange.Type.ADDED -> {
                                        val newMessage = StoryDocumentChange.document.data
                                        println("New message added: $newMessage")
                                        // Handle the added message as needed
                                        getOnlyStory()
                                        Log.i("updated","document changes")
                                    }
                                    // Handle other document change types as needed
                                    else -> {}
                                }
                            }
                        }
                    }
                }
            } else {
                Log.i("ChatActivity", "Users collection is not available")
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("currentUser", CurrentUser)
        binding = FragmentStatusBinding.inflate(layoutInflater, container, false)

        //api calling
        getOnlyStory()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        binding.ProfilePicture.setOnClickListener {
            if (checkCameraPermission()) {
                // Camera permission is already granted
                // Perform your camera-related operations here
                ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(10)

            } else {
                // Request camera permission
                requestCameraPermission()
            }
        }
    }


    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.Recycleviewstatusview.layoutManager = layoutManager
    }

    override fun onItemClick(position: Int) {
        if (position >= 0 && position < stories.size) {
            Log.i("size", stories.size.toString())
            var status: ArrayList<Story> = stories[position].stories
            var username = stories[position].name
            var profileImg = stories[position].profileImg
            Log.e("StatusFragment", "valid position: $status")
            val intent = Intent(context, StoryViewActivity::class.java)
            intent.putExtra("story", status)
            intent.putExtra("username", username)
            intent.putExtra("profileImg", profileImg)
            startActivity(intent)
        } else {
            Log.e("StatusFragment", "Invalid position: $position")
        }
    }

    private fun getOnlyStory() {
        val retrofit = Retrofit.createRetrofitInstance(baseUrl)
        val apiService = retrofit.create(StoryApiService::class.java)
        val call: Call<ArrayList<UserStory>> = apiService.getOnlyStory()
        call.enqueue(object : Callback<ArrayList<UserStory>> {
            override fun onResponse(
                call: Call<ArrayList<UserStory>>,
                response: Response<ArrayList<UserStory>>
            ) {
                if (response.isSuccessful) {
                    stories = response.body()!!
                    if (stories != null) {
                        Log.i("[getStories]Success", stories.toString())
                        //bind data with adapter
                        binding.Recycleviewstatusview.layoutManager = LinearLayoutManager(activity)
                        val adapter = StoryAdapter(
                            stories,
                            activity!!.applicationContext,
                            this@StatusFragment
                        )
                        binding.Recycleviewstatusview.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Log.i("getStories[onResponse]Error", response.message())
                }
            }

            override fun onFailure(call: Call<ArrayList<UserStory>>, t: Throwable) {
                Log.i("getStories[onFailure]", t.message.toString())
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            Log.i("onActivityResult", "Image is selected")
            selectedImageUri = data?.data
            bitmap =
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
            Log.i("image",bitmap.toString())
            SaveImageFireStore()
        }

    }

    private suspend fun uploadImgInStorage(): String? = suspendCancellableCoroutine { continuation ->
        try {
            val outputStream = ByteArrayOutputStream()
            if (bitmap != null && !bitmap!!.isRecycled) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val bytes: ByteArray = outputStream.toByteArray()
                val apiService = retrofit.create(StoryApiService::class.java)
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

    private suspend fun addStory(story: Story): String? =
        suspendCancellableCoroutine { continuation ->
            try {
                val retrofit = Retrofit.createRetrofitInstance(baseUrl)
                val apiService = retrofit.create(StoryApiService::class.java)
                val call = apiService.addStory(story)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>, response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            var result = response.body()
                            if (response.code() == 200) {
                                continuation.resume("added Story")
                            }
                            Log.i("success[addStory]", "added>>>>>>>>>>>>>" + result.toString())
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

    private fun SaveImageFireStore() {
        lifecycleScope.launch {
            if (selectedImageUri != null) {
                // If an image is selected, perform image-related tasks
                println("Image URI: $selectedImageUri")
                val uploadedStoryUrl = uploadImgInStorage()
                Log.i("result", uploadedStoryUrl.toString())
                if (uploadedStoryUrl != null) {
                    //api save
                    story = Story(
                        uid = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                        story = uploadedStoryUrl.toString(),
                        timestamp = date.time
                    )
                    var result = addStory(story)
                    if (result != null) {
                        Toast.makeText(
                            requireContext(),
                            "Your story is uploaded successfully!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.i("UserSaved", result.toString())
                        Toast.makeText(
                            requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(), "Something went wrong!!", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("setup Activity", "permission granted")
                    // Permission granted
                    // Proceed with initializing your camera
                    ImagePicker.with(this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(10)
                } else {
                    Log.i("setup Activity", "permission denied")

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            context as Activity, Manifest.permission.CAMERA
                        )
                    ) {
                        // Explain to the user why the permission is needed and redirect to the app settings
                        showPermissionExplanationDialog()
                    } else {
                        // Inform the user or handle this case accordingly
                        Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    fun showPermissionExplanationDialog() {
        AlertDialog.Builder(context).setTitle("Camera Permission Required")
            .setMessage("This app requires camera permission to function properly. Please grant the permission in the app settings.")
            .setPositiveButton("Go to Settings") { dialog, which ->
                navigateToAppSettings()
            }.setNegativeButton("Cancel") { dialog, which ->
            }.show()
    }

    // Open the app settings
    private fun navigateToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context?.packageName, null)
        intent.data = uri
        (context as Activity).startActivityForResult(intent, APP_SETTINGS_REQUEST_CODE)
    }

}