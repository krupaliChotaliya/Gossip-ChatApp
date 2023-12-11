package com.android.chatsapp.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.chatsapp.adapters.TopStatusAdapter
import com.android.chatsapp.api.Retrofit
import com.android.chatsapp.api.UserApiService
import com.android.chatsapp.databinding.FragmentStatusBinding
import com.android.chatsapp.model.UserStatus
import com.google.firebase.auth.FirebaseAuth


class StatusFragment : Fragment() {

    private lateinit var binding: FragmentStatusBinding
    private  lateinit var userList: ArrayList<UserStatus>
    private var baseUrl = "http://192.168.125.10:8080/"
    private val apiService = Retrofit.createRetrofitInstance(baseUrl).create(UserApiService::class.java)
    private var CurrentUser: String = FirebaseAuth.getInstance().uid.toString()
    private lateinit var topStatusAdapter:TopStatusAdapter
    private lateinit var userStatus: ArrayList<UserStatus>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            binding.userStatus.setOnClickListener(View.OnClickListener {
                Log.i("clicked","enter>>>>>>>>>>>")
//                if (ContextCompat.checkSelfPermission(
//                        requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    ) == PackageManager.PERMISSION_GRANTED
//                ) {
//                    Intent(Intent.ACTION_PICK).also {
//                        it.type = "image/*"
//                        val minTypes = arrayOf("image/jpeg", "image/png")
//                        it.putExtra(Intent.EXTRA_MIME_TYPES, minTypes)
//                    }
//                } else {
//                    requestPermissions(
//                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                        1
//                    )
//                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatusBinding.inflate(inflater,container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        topStatusAdapter=TopStatusAdapter(requireContext(),userList)
        binding.Recycleviewstatusview.adapter=topStatusAdapter
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.Recycleviewstatusview.layoutManager = layoutManager
    }

    private fun uploadImg(){

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context,"granted",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context,"not granted",Toast.LENGTH_SHORT).show()
        }
    }
}