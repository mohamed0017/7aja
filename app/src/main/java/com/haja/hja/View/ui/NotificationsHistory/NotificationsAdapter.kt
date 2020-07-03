package com.haja.hja.View.ui.NotificationsHistory

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haja.hja.R
import com.haja.hja.Service.model.NotificationData
import com.haja.hja.View.ui.ProductDetails.ProductDetailsActivity
import kotlinx.android.synthetic.main.notification_item.view.*

class NotificationsAdapter(
    private val notifications: ArrayList<NotificationData?>?
) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

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

        holder.itemView.setOnClickListener {
            if (notification?.withId != "0") {
                val intent = Intent(holder.itemView.context, ProductDetailsActivity::class.java)
                intent.putExtra("productId", notification?.withId)
                intent.putExtra("fromNotification", true)
                holder.itemView.context.startActivity(intent)
            } else {
                val url = notification.urlLink
                if (url != null)
                if (url.contains("http")) {
                    val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    holder.itemView.context.startActivity(openUrlIntent)
                }
            }
        }
        Log.e(" notification?.type", notification?.withId)
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