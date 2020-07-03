package com.haja.hja.View.ui.faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haja.hja.R
import com.haja.hja.Service.model.FaqDataModel
import kotlinx.android.synthetic.main.notification_item.view.*

class FaqAdapter (
    private val questions: ArrayList<FaqDataModel?>?
) : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.faq_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return questions?.size ?: 0
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val notification = questions?.get(position)

        holder.title.text = notification?.name
        holder.description.text = notification?.details
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.notificationItemTitle
        val description: TextView = itemView.notificationItemDescription

    }
}