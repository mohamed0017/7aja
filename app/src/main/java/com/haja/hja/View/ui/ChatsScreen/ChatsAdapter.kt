package com.haja.hja.View.ui.ChatsScreen

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haja.hja.R
import com.haja.hja.Service.model.ChatsDataModel
import com.haja.hja.View.ui.ChatScreen.ChatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.message_item.view.*

class ChatsAdapter(private val context: Context,
                   private val chats: List<ChatsDataModel?>?
) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return chats?.size ?: 0
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val chat = chats?.get(position)

        holder.title.text = chat?.name
        holder.description.text = chat?.message
        holder.count.text = chat?.countChat.toString()
        if (chat?.img != null) {
            Picasso.get().load(chat.img).into(holder.imageView)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("user2_id" , chat?.userId.toString())
            intent.putExtra("user2_name" , chat?.name.toString())
            context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.chatItemTitle
        val description: TextView = itemView.chatItemDescription
        val count: TextView = itemView.chatItemCount
        val imageView: ImageView = itemView.chatItemImg

    }
}