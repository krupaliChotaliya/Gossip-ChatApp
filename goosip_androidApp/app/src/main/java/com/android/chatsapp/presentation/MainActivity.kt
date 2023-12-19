package com.android.chatsapp.presentation

import android.content.Intent
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
import com.android.chatsapp.databinding.ActivityMainBinding
import com.android.chatsapp.fragments.CallsFragment
import com.android.chatsapp.fragments.ChatsFragment
import com.android.chatsapp.fragments.StatusFragment
import com.android.chatsapp.helper.SharedPrefManager
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        //check whether user is logged in or not
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser == null) {
            Log.i("not login", firebaseAuth.currentUser.toString())
            Toast.makeText(
                this,
                "You are not logged In",
                Toast.LENGTH_SHORT
            ).show()
            Intent(
                this,
                PhoneNoVerificationActivity::class.java
            )
            startActivity(intent)
        }
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val fragmentTitles = listOf("Chats", "Status", "Calls")
        val fragments: ArrayList<Fragment> = arrayListOf(
            ChatsFragment(),
            StatusFragment(),
            CallsFragment()
        )
        val adapter = ViewPagerAdapter(fragments, fragmentTitles, supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(binding.tablayout, viewPager) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()
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

}


