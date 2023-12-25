package com.android.chatsapp.presentation

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.chatsapp.databinding.ActivityOtpverifyBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
private lateinit var loadingbar: ProgressDialog

class OTPVerifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpverifyBinding
    private lateinit var auth: FirebaseAuth
    private var verificationId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpverifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //check authentication
        checkAuthenctionStatus()
    }

    private fun checkAuthenctionStatus() {
        try {
            auth = FirebaseAuth.getInstance()
            val phoneno = intent.getStringExtra("phonenumber")
            binding.lblphoneno.text = "Verify $phoneno"

            loadingbar = ProgressDialog(this@OTPVerifyActivity)
            loadingbar.setMessage("Sending the OTP to your Phone number")
            loadingbar.show()

            val options: PhoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneno.toString())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this@OTPVerifyActivity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    }
                    override fun onVerificationFailed(e: FirebaseException) {
                        Log.i("Not verify", "failed");
                        Toast.makeText(this@OTPVerifyActivity, "Failed", Toast.LENGTH_SHORT).show()
                        val intent = Intent(
                            this@OTPVerifyActivity,
                            PhoneNoVerificationActivity::class.java
                        )
                        startActivity(intent)
                    }
                    override fun onCodeSent(
                        verifyId: String,
                        forceResendingToken: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verifyId, forceResendingToken)
                        verificationId = verifyId
                    }
                }).build()

            PhoneAuthProvider.verifyPhoneNumber(options)
            binding.otpView.setOtpCompletionListener { otp ->
                if (verificationId != null && otp != null) {
                    val credential: PhoneAuthCredential =
                        PhoneAuthProvider.getCredential(verificationId, otp)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
                            override fun onComplete(task: Task<AuthResult?>) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(
                                        this@OTPVerifyActivity,
                                        "You are Successfully Logged In!!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    loadingbar.dismiss()
                                    val intent = Intent(
                                        this@OTPVerifyActivity,
                                        SetupProfileActivity::class.java
                                    )
                                    startActivity(intent)
                                    finishAffinity()
                                } else {
                                    Toast.makeText(
                                        this@OTPVerifyActivity,
                                        "Failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        })
                } else {
                    Log.i("Not verify", "Not verify");
                    Toast.makeText(this@OTPVerifyActivity, "Not verify", Toast.LENGTH_SHORT).show()
                    Intent(
                        this@OTPVerifyActivity,
                        PhoneNoVerificationActivity::class.java
                    )
                }
            }
        } catch (e: Exception) {
            Log.i("catch block", e.toString());
            Toast.makeText(
                this@OTPVerifyActivity,
                "catch block",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

