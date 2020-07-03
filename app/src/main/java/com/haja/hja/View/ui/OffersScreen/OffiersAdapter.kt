package com.haja.hja.View.ui.OffersScreen

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haja.hja.OnItemClick
import com.haja.hja.R
import com.haja.hja.Service.ApiService
import com.haja.hja.Service.model.ProductData
import com.haja.hja.View.ui.AdScreen.AdActivity
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
        val product = products?.get(position)
        holder.title.text = product?.name
        holder.description.text = product?.description

        if (product?.imgs?.isNotEmpty()!!) {
            Picasso.get().load(ApiService.IMAGEBASEURL + product.imgs[0]?.img)
                .placeholder(context.resources.getDrawable(R.drawable.placeholder))
                .error(context.resources.getDrawable(R.drawable.placeholder)).into(holder.imageView)
        }

        holder.itemView.setOnClickListener {
            if (product.imgs.isNotEmpty()) {
                val intent = Intent(context, AdActivity::class.java)
                intent.putExtra("offerId", product.id.toString())
                intent.putExtra("offerLikes", product.likes.toString())
                intent.putExtra("offerName", product.name)
                intent.putExtra("numViews", product.numViews.toString())
                intent.putExtra("isLike", product.isLike)
                intent.putExtra("mobile", product.mobile)
                Log.i("isLike",  product.isLike.toString())
                Log.i("isLike",  product.name.toString())
                Log.i("isLike",  product.id.toString())
                intent.putExtra("offerImage", product.imgs[0]?.img)
                intent.putExtra("fromOffersScreen", true)
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