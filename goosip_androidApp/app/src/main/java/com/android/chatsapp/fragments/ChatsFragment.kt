package com.android.chatsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.chatsapp.adapters.UserAdapter
import com.android.chatsapp.api.Retrofit
import com.android.chatsapp.api.UserApiService
import com.android.chatsapp.databinding.FragmentChatsBinding
import com.android.chatsapp.model.User
import com.android.chatsapp.presentation.ChatActivity
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatsFragment : Fragment(),UserAdapter.RecycleViewEvent   {

    private lateinit var binding: FragmentChatsBinding
    private  lateinit var userList: ArrayList<User>
    private var baseUrl = "http://192.168.205.10:8080/"
    private val apiService = Retrofit.createRetrofitInstance(baseUrl).create(UserApiService::class.java)
    private var CurrentUser: String = FirebaseAuth.getInstance().uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("currentUser",CurrentUser)
        binding = FragmentChatsBinding.inflate(layoutInflater,container, false)
        getUsers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        getUsers()
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.chatsRecycleView.layoutManager = layoutManager
    }


    override fun onItemClick(position: Int) {
        if (position >= 0 && position < userList.size) {
            var clickedUser: User = userList[position]
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("user", clickedUser)
            startActivity(intent)
        } else {
            Log.e("ChatsFragment", "Invalid position: $position")
        }
    }


    override fun onStart() {
        super.onStart()
        getUsers()
    }

   private fun getUsers() {
        val call: Call<ArrayList<User>> = apiService.getAllUsers(CurrentUser)
        call.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                if (response.isSuccessful) {
                    userList= response.body()!!
                    if (userList != null) {
                        Log.i("[getUsers]Success", userList.toString())
                        //bind data with adapter
                        binding.chatsRecycleView.layoutManager = LinearLayoutManager(activity)
                        val adapter = UserAdapter(userList,activity!!.applicationContext,this@ChatsFragment)
                        binding.chatsRecycleView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    // Handle error
                    Log.i("getUsers[onResponse]Error", response.message())
                }
            }
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                // Handle failure
                Log.i("getUsers[onFailure]", t.message.toString())
            }
        })
    }
}