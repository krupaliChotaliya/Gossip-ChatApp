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
import com.android.chatsapp.model.UserStory
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class StoryAdapter(
    private val list: ArrayList<UserStory>,
    private val context: Context,
    private val listener: RecycleViewEvent
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, private val listener: RecycleViewEvent) :
        RecyclerView.ViewHolder(itemView), OnClickListener {
        val username: TextView
        val receiverStatus: CircleImageView
        init {
            username = itemView.findViewById(R.id.StoryUserName)
            receiverStatus=itemView.findViewById(R.id.receiverStatus)
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
            LayoutInflater.from(context).inflate(R.layout.item_statusview, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        Log.i("size",list.size.toString())
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = list[position].name
        Picasso.get().load(list[position].profileImg).into(holder.receiverStatus)
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    interface RecycleViewEvent {
        fun onItemClick(position: Int)
    }
}