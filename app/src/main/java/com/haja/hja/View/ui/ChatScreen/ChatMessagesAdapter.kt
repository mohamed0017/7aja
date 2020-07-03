package com.haja.hja.View.ui.ChatScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haja.hja.OnItemClickWithId
import com.haja.hja.R
import com.haja.hja.Service.model.ChatMessagesDataModel
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.USERID
import kotlinx.android.synthetic.main.chat_message_item.view.*

class ChatMessagesAdapter(
    private val context: Context,
    private val onItemClick: OnItemClickWithId
) :
    RecyclerView.Adapter<ChatMessagesAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var messages: ArrayList<ChatMessagesDataModel>? = ArrayList()

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.chat_message_item, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = messages?.get(position)
        holder.bind(data)
    }

    // total number of rows
    override fun getItemCount(): Int {
        return if (messages.isNullOrEmpty())
            0
        else
            messages?.size!!
    }

    fun setMessagesList(newMessages: ArrayList<ChatMessagesDataModel>) {
        messages?.clear()
        messages = newMessages
        messages?.reverse()
    }

    fun addItemToMessages(message: ChatMessagesDataModel) {
        messages?.add(message)
    }

    fun removeMessage(position: Int) {
        messages?.removeAt(position)
    }

    inner class ViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(data: ChatMessagesDataModel?) {
            val userId = SharedPreferenceUtil(context).getString(USERID, "0")
            if (data?.userId == userId)
                initMessageItemIfReplyFromStudent()
            else
                initMessageItemIfReplyFromAdmin()

            itemView.messageComment.text = data?.message
            itemView.setOnLongClickListener {
                if (data?.userId == userId)
                    onItemClick.onClick(data?.id!!, adapterPosition)
                true
            }
        }

        private fun initMessageItemIfReplyFromAdmin() {
            itemView.messageComment.background = context.resources.getDrawable(R.drawable.rounded_react_gray)
            itemView.messageComment.setTextColor(context.resources.getColor(R.color.colorPrimaryDark))
            itemView.messageCorner.background = context.resources.getDrawable(R.drawable.corner_gray)
            //   itemView.userIcon.setImageResource(R.drawable.logo_message)
            itemView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }

        private fun initMessageItemIfReplyFromStudent() {
            itemView.messageComment.background = context.resources.getDrawable(R.drawable.rounded_react)
            itemView.messageCorner.background = context.resources.getDrawable(R.drawable.corner)
            itemView.messageComment.setTextColor(context.resources.getColor(R.color.white))
            // itemView.userIcon.setImageResource(R.drawable.profile)
            itemView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }
    }
}