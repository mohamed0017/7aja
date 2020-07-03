package com.haja.hja.View.ui.DelegatesScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haja.hja.R
import com.haja.hja.Service.ApiService
import com.haja.hja.Service.model.DelegatesdataModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.delegate_item.view.*

class DelegatesAdapter(
    private val context: Context,
    private var delegates: ArrayList<DelegatesdataModel>
) : RecyclerView.Adapter<DelegatesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.delegate_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return if (delegates.isNullOrEmpty())
            return 0
        else delegates.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val delegate = delegates.get(position)
        holder.name.text = delegate.name
        holder.phone.text = delegate.mobile

        if (delegate.img != null) {
            Picasso.get().load(ApiService.IMAGEBASEURL + delegate.img)
                .placeholder(context.resources.getDrawable(R.drawable.app_icon))
                .error(context.resources.getDrawable(R.drawable.app_icon)).into(holder.delegateImg)
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.delegateName
        val phone: TextView = itemView.delegatePhone
        val delegateImg = itemView.delegateImg
    }
}