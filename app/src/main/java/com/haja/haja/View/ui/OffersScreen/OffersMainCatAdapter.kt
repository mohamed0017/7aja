package com.haja.haja.View.ui.OffersScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.R
import com.haja.haja.Service.model.OfferCategoriesData
import kotlinx.android.synthetic.main.offers_categories_item.view.*

class OffersMainCatAdapter(
    private val context: Context,
    private val offersCategories: List<OfferCategoriesData?>?,
    private val offersFragment: OffersFragment
) : RecyclerView.Adapter<OffersMainCatAdapter.ViewHolder>() {

    var lastItemPossion = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.offers_categories_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return offersCategories?.size!!
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val cat = offersCategories?.get(position)
        holder.title.text = cat?.name
        if (lastItemPossion == position) {
            holder.title.background = context.resources.getDrawable(R.drawable.rounded_corners)
            holder.title.setTextColor(context.resources.getColor(R.color.colorPrimaryDark))
        } else {
            holder.title.background = null
            holder.title.setTextColor(context.resources.getColor(R.color.white))
        }

        holder.itemView.setOnClickListener {
            lastItemPossion = position
            notifyDataSetChanged()
                cat?.let { it1 -> offersFragment.onClick( it1, position) }
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.offerCatName

    }
}