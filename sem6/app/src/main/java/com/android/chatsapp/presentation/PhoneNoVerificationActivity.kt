package com.android.chatsapp.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.chatsapp.databinding.ActivityPhoneNoVerificationBinding
import com.google.firebase.auth.FirebaseAuth

class PhoneNoVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhoneNoVerificationBinding;
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneNoVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null) {
            Log.i("loggedIn", firebaseAuth.currentUser.toString())
            val intent = Intent(
                this,
                MainActivity::class.java
            )
            startActivity(intent)
            finishAffinity()
        }

        binding.continuebtnPhoneverify.setOnClickListener {
            val intent = Intent(this, OTPVerifyActivity::class.java)
            intent.putExtra("phonenumber", binding.phoneno.text.toString())
            startActivity(intent)
            finish()
        }
    }
}