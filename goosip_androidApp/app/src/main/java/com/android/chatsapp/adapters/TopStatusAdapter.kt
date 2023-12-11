package com.android.chatsapp.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.chatsapp.R
import com.android.chatsapp.databinding.ItemStatusviewBinding
import com.android.chatsapp.model.UserStatus

class TopStatusAdapter(
    private val context: Context,
    private val userStatus: ArrayList<UserStatus>
) : RecyclerView.Adapter<TopStatusAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var binding: ItemStatusviewBinding = ItemStatusviewBinding.bind(itemView)

        override fun onClick(p0: View?) {

        }

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View =
            LayoutInflater.from(context).inflate(R.layout.item_statusview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userStatus.size
    }



}