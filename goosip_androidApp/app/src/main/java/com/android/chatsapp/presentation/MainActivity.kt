package com.android.chatsapp.presentation

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.android.chatsapp.R
import com.android.chatsapp.adapters.ViewPagerAdapter
import com.android.chatsapp.api.Retrofit
import com.android.chatsapp.api.UserApiService
import com.android.chatsapp.databinding.ActivityMainBinding
import com.android.chatsapp.fragments.ChatsFragment
import com.android.chatsapp.fragments.StatusFragment
import com.android.chatsapp.helper.Constants
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private var baseUrl = Constants.API_BASE_URL
    private var userId: String = ""
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        firebaseAuth = FirebaseAuth.getInstance()

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
//        val fragmentTitles = listOf("Chats", "Status", "Calls")
        val fragmentTitles = listOf("Chats", "Status")
        val fragments: ArrayList<Fragment> = arrayListOf(
            ChatsFragment(),
            StatusFragment(),
//            CallsFragment()
        )
        val adapter = ViewPagerAdapter(fragments, fragmentTitles, supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(binding.tablayout, viewPager) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()

        // Customize the color of the overflow menu icon
        val overflowIcon = binding.myToolbar.overflowIcon
        overflowIcon?.setColorFilter(resources.getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP)


        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            userId = currentUser.uid
            //fcm -Token generation
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("Token", "getInstanceId failed", task.exception)
                        return@addOnCompleteListener
                    }
                    Log.w("Token", task.result)
                    token = task.result
                    updateField("fcmToken", token)
                }

        }else{
            Toast.makeText(
                this,
                "You are not logged In",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(
                this,
                PhoneNoVerificationActivity::class.java
            )
            startActivity(intent)
            finishAffinity()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(this@MainActivity, SetupProfileActivity::class.java)
                startActivity(intent)
            }

            R.id.logout -> {
                firebaseAuth.signOut()
                Toast.makeText(
                    this,
                    "You are logged Out",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(
                    this,
                    PhoneNoVerificationActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateField(fieldName: String, value: String) {
        try {
            val retrofit = Retrofit.createRetrofitInstance(baseUrl)
            val apiService = retrofit.create(UserApiService::class.java)
            val call: Call<ResponseBody> =
                apiService.updateField(userId, fieldName, value)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>, response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.string()
                        Log.i("Success>>>", responseBody ?: "")
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

}


