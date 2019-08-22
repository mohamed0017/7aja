package com.haja.haja.View.ui.Products

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.OnProductItemClicked
import com.haja.haja.R
import com.haja.haja.Service.ApiService
import com.haja.haja.Service.model.ProductData
import com.haja.haja.Utils.inTransaction
import com.haja.haja.View.ui.MyFavorites.FavoritesFragment
import com.haja.haja.View.ui.ProductDetails.ProductDetailsFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_item.view.*

class ProductsAdapter(
    private val context: Context,
    private val fragmentManager: FragmentManager?,
    private val productsFragment: OnProductItemClicked
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private var products: ArrayList<ProductData?>? = null

    fun setItemList(products: List<ProductData>?) {
        this.products = products as ArrayList<ProductData?>
    }

    fun removeItemFromList(position: Int) {
        Log.i("position","$position")
        products?.removeAt(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return products?.size ?: 0
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val product = products?.get(position)
        if (product?.imgs?.isNotEmpty()!!) {
            Picasso.get().load(ApiService.IMAGEBASEURL + product.imgs[0]?.img)
                .placeholder(context.resources.getDrawable(R.drawable.placeholder))
                .error(context.resources.getDrawable(R.drawable.placeholder)).into(holder.imageView)
        }
        if (productsFragment is FavoritesFragment)
            holder.favIcon.setImageResource(R.mipmap.fav_hov_1mdpi)

        if (product.isFavorite != null)
            holder.favIcon.setImageResource(R.mipmap.fav_hov_1mdpi)

        holder.title.text = product.name
        holder.description.text = product.description
        holder.date.text = product.createdAt
        holder.favIcon.setOnClickListener {
            productsFragment.onClick(position, product, holder.favIcon)
        }
        holder.itemView.setOnClickListener {
            fragmentManager?.inTransaction {
                replace(R.id.mainContainer, ProductDetailsFragment.newInstance(product)).addToBackStack("staticContent")
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.productItemTitle
        val description: TextView = itemView.productItemDescription
        val date: TextView = itemView.productItemDate
        // val cost : TextView = itemView.productItemCost
        val imageView: ImageView = itemView.productItemImg
        val favIcon = itemView.productItemFav

    }
}