package com.haja.haja.View.ui.OffersScreen

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.OnItemClick
import com.haja.haja.R
import com.haja.haja.Service.ApiService
import com.haja.haja.Service.model.ProductData
import com.haja.haja.View.ui.AdScreen.AdActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.offer_item.view.*

class OffiersAdapter(
    private val context: Context,
    private val offersFragment: OnItemClick
) : RecyclerView.Adapter<OffiersAdapter.ViewHolder>() {

    private var products: ArrayList<ProductData?>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.offer_item, parent, false)
        return ViewHolder(v)
    }

    fun clearList(){
        products?.clear()
    }
    fun setNewProducts(products: ArrayList<ProductData?>?) {
        this.products = products
    }

    override fun getItemCount(): Int {
        return if (products.isNullOrEmpty())
            return 0
        else products?.size!!
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val cat = products?.get(position)
        holder.title.text = cat?.name
        holder.description.text = cat?.description

        if (cat?.imgs?.isNotEmpty()!!) {
            Picasso.get().load(ApiService.IMAGEBASEURL + cat.imgs[0]?.img)
                .placeholder(context.resources.getDrawable(R.drawable.placeholder))
                .error(context.resources.getDrawable(R.drawable.placeholder)).into(holder.imageView)
        }

        holder.itemView.setOnClickListener {
            if (cat.imgs.isNotEmpty()) {
                val intent = Intent(context, AdActivity::class.java)
                intent.putExtra("offerName", cat.name)
                intent.putExtra("offerImage", cat.imgs[0]?.img)
                context.startActivity(intent)
            }

        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.offerItemTitle
        val description: TextView = itemView.offerItemDescription
        val imageView = itemView.offerItemImg
    }
}