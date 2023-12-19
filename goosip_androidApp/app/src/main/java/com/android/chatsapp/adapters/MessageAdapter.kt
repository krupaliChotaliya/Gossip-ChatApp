package com.android.chatsapp.adapters

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.chatsapp.R
import com.android.chatsapp.databinding.ItemReceiveBinding
import com.android.chatsapp.databinding.ItemSendBinding
import com.android.chatsapp.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.Calendar
import java.util.Locale

class MessageAdapter(private val context: Context, var messages: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var item_send: Int = 1
    private var item_receive: Int = 2

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemSendBinding = ItemSendBinding.bind(itemView)
    }

    class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemReceiveBinding = ItemReceiveBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == item_send) {
            var view: View =
                LayoutInflater.from(context).inflate(R.layout.item_send, parent, false)
            return SentViewHolder(view)
        } else {
            var view: View =
                LayoutInflater.from(context).inflate(R.layout.item_receive, parent, false)
            return ReceiverViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        var message: Message = messages.get(position)
        return if (FirebaseAuth.getInstance().uid.equals(message.senderId)) {
            item_send
        } else {
            item_receive
        }
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var message: Message = messages[position]

        when (holder) {
            is SentViewHolder -> {
                var viewHolder: SentViewHolder = holder
                if (message.type.equals("text")) {
                    viewHolder.binding.message.text = message.message
                    viewHolder.binding.time.text = getShortDate(message.timestamp)


                    //visibility
                    viewHolder.binding.message.visibility=View.VISIBLE
                    viewHolder.binding.messageRoot.visibility = View.GONE

                } else {
                    Picasso
                        .get()
                        .load(message.message)
                        .into(viewHolder.binding.imageViewMessageImage)
                    viewHolder.binding.time.text = getShortDate(message.timestamp)
                    //visibility
                    viewHolder.binding.message.visibility=View.GONE
                    viewHolder.binding.messageRoot.visibility = View.VISIBLE
                }
            }
            is ReceiverViewHolder -> {
                var viewHolder: ReceiverViewHolder = holder
                if (message.type.equals("text")) {
                    viewHolder.binding.message.text = message.message
                    viewHolder.binding.time.text = getShortDate(message.timestamp)


                    viewHolder.binding.message.visibility=View.VISIBLE
                    viewHolder.binding.messageRoot.visibility = View.GONE

                } else {
                    Picasso
                        .get()
                        .load(message.message)
                        .into(viewHolder.binding.imageViewMessageImage)
                    viewHolder.binding.time.text = getShortDate(message.timestamp)

                    //visibility
                    viewHolder.binding.message.visibility=View.GONE
                    viewHolder.binding.messageRoot.visibility = View.VISIBLE
                }
            }
        }
    }

    fun getShortDate(ts: Long?): String {
        if (ts == null) return ""
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = ts
        val dateFormat = SimpleDateFormat("hh:mm a MMMM d", Locale.getDefault())
        dateFormat.isLenient = false
        return dateFormat.format(calendar.time)
    }
}
