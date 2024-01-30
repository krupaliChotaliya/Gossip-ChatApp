package com.android.chatsapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.android.chatsapp.R
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000 // 2 seconds
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        timer = object : CountDownTimer(SPLASH_TIME_OUT, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    val intent = Intent(
                        this@SplashScreen,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                    finishAffinity()
                }else{
                    val intent = Intent(this@SplashScreen, PhoneNoVerificationActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        timer.start()
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }
}