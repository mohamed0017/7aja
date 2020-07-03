package com.haja.hja.View.ui.MoreScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haja.hja.R
import com.haja.hja.Service.ApiService
import com.haja.hja.Service.model.SocialMediasModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.social_item.view.*

class SocialMediaAdapter(
    private val context: Context,
    private val links: List<SocialMediasModel?>?
) : RecyclerView.Adapter<SocialMediaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.social_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return if (links.isNullOrEmpty())
            return 0
        else links?.size!!
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val link = links?.get(position)
        holder.title.text = link?.name
        Picasso.get().load(ApiService.IMAGEBASEURL + link?.img).into(holder.imageView)

        holder.itemView.setOnClickListener {
            val url = link?.urlL
            if (url?.contains("http")!!) {
                val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                openUrlIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(openUrlIntent)
            }
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.socialName
        val imageView = itemView.socialImg
    }
}