package com.haja.haja.View.ui.NotificationsHistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.R
import com.haja.haja.Service.model.NotificationData
import kotlinx.android.synthetic.main.notification_item.view.*

class NotificationsAdapter(
    private val notifications: ArrayList<NotificationData?>?)
    : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return notifications?.size ?: 0
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val notification = notifications?.get(position)

        holder.title.text = notification?.name
        holder.description.text = notification?.details
        holder.date.text = notification?.createdAt?.substringBefore(" ")
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.notificationItemTitle
        val description: TextView = itemView.notificationItemDescription
        val date: TextView = itemView.notificationItemDate
        val imageView: ImageView = itemView.notificationItemImg

    }
}