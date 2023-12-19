package com.android.chatsapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.android.chatsapp.R
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfileImagePicker : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var button: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_image_picker)
        Log.i("create","enter>>>>>>>>>")
        imageView = findViewById(R.id.imageView2)
        button = findViewById(R.id.floatingActionButton2)

        button.setOnClickListener {
            Log.i("float","enter>>>>>>>>>")
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("onActivityResult","enter>>>>>>>>>")
        super.onActivityResult(requestCode, resultCode, data)
        imageView.setImageURI(data?.data)
    }
}