package com.haja.haja.View.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.R
import com.haja.haja.Service.model.AttributesModel
import kotlinx.android.synthetic.main.attributes_item.view.*

class AttributsAdapter (
    private val attributes: List<AttributesModel?>?) : RecyclerView.Adapter<AttributsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.attributes_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return attributes?.size!!
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val product = attributes?.get(position)
        holder.title.text = product?.name
        holder.value.text = product?.value

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.attributeName
        val value: TextView = itemView.attributeValue

    }
}