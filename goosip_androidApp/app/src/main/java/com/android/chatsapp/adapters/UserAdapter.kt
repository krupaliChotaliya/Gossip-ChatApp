package com.android.chatsapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.chatsapp.R
import com.android.chatsapp.model.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(
    private val list: ArrayList<User>,
    private val context: Context,
    private val listener: RecycleViewEvent
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, private val listener: RecycleViewEvent) :
        RecyclerView.ViewHolder(itemView), OnClickListener {
        val username: TextView
        val lastmesage: TextView
        val profilePicture: CircleImageView

        init {
            username = itemView.findViewById(R.id.userName)
            lastmesage = itemView.findViewById(R.id.lastmsg)
            profilePicture = itemView.findViewById(R.id.ProfilePicture)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            Log.i("redirect", "onClick called for position $position")
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
            Log.i("redirect", "enter_____________")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View =
            LayoutInflater.from(context).inflate(R.layout.show_user_layout, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = list[position].name
        holder.lastmesage.text = list[position].status
        Picasso
            .get()
            .load(list[position].profileImg)
            .into(holder.profilePicture)

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    interface RecycleViewEvent {
        fun onItemClick(position: Int)
    }
}