package com.android.chatsapp.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.chatsapp.databinding.ActivityPhoneNoVerificationBinding
import com.google.firebase.auth.FirebaseAuth

class PhoneNoVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhoneNoVerificationBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var PhoneNumber_pattern = "^\\+91\\d{10}$"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneNoVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("PhoneVerify", "enter>>>>>>>>>>")

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
            val phoneNumber = binding.phoneno.text.toString().trim()
            if (!phoneNumber.matches(Regex(PhoneNumber_pattern))) {
                Toast.makeText(this, "Please enter Valid Phone number.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, OTPVerifyActivity::class.java)
                intent.putExtra("phone-number", binding.phoneno.text.toString())
                startActivity(intent)
                finish()
            }

        }
    }
}