package com.haja.hja.View.ui.Products

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.haja.hja.OnProductItemClicked
import com.haja.hja.R
import com.haja.hja.Service.ApiService
import com.haja.hja.Service.model.ProductData
import com.haja.hja.Utils.inTransaction
import com.haja.hja.View.ui.EditProduct.EditProductActivity
import com.haja.hja.View.ui.MyAdsScreen.MyAdsFragment
import com.haja.hja.View.ui.MyFavorites.FavoritesFragment
import com.haja.hja.View.ui.ProductDetails.ProductDetailsActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_item.view.*


class ProductsAdapter(
    private val context: Context,
    private val fragmentManager: FragmentManager?,
    private val productsFragment: OnProductItemClicked
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private var products = ArrayList<ProductData?>()

    fun setItemList(products: List<ProductData>?) {
        this.products.addAll(products as ArrayList<ProductData?> )
    }

    fun removeItemFromList(position: Int) {
        products.removeAt(position)
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
        return products.size ?: 0
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val product = products[position]!!
        initProductData(product,holder)
        seFavoriteIcon(holder , product)
        setDeleteIconIfIsMyProducts(holder , product, position)

        holder.favIcon.setOnClickListener {
            productsFragment.onClick(position, product, holder.favIcon)
        }

        if (productsFragment is FavoritesFragment) {
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra("productId", product.productId)
                context.startActivity(intent)
            }
        } else {
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra("productId", product.id)
                context.startActivity(intent)
            }
        }

        holder.mCurrentPosition = position

    }

    private fun initProductData(product: ProductData, holder: ProductsAdapter.ViewHolder) {
        var date = product.createdAt
        date = date?.substringBefore(" ")
        holder.title.text = product.name
        holder.description.text = product.description
        holder.date.text = date
        holder.cost.text = "${product.price.toString()}  ${context.resources.getString(R.string.price_unit)}"
        if (product?.imgs?.isNotEmpty()!!) {
            Picasso.get().load(ApiService.IMAGEBASEURL + product.imgs[0]?.img)
                .placeholder(context.resources.getDrawable(R.drawable.placeholder))
                .error(context.resources.getDrawable(R.drawable.placeholder)).into(holder.imageView)
        }
    }

    private fun setDeleteIconIfIsMyProducts(
        holder: ViewHolder,
        product: ProductData,
        position: Int
    ) {

        if (productsFragment is MyAdsFragment)
        {
            if (product.isPublished == "N")
                holder.editItemDraft.visibility = View.VISIBLE
            else
                holder.editItemDraft.visibility = View.GONE

            holder.favIcon.visibility = View.GONE
            holder.deleteIcon.visibility = View.VISIBLE
            holder.editItem.visibility = View.VISIBLE
            holder.deleteIcon.setOnClickListener {
                productsFragment.onClick(position, product, holder.favIcon)
            }
            holder.editItem.setOnClickListener {
                fragmentManager?.inTransaction {
                    replace(R.id.mainContainer, EditProductActivity.newInstance(product.id!!)).addToBackStack("dd")
                }
            }
            holder.productItemStar.visibility = View.GONE
        }else{
            if (product.isSpecial == "0")
                holder.productItemStar.visibility = View.GONE
            else
                holder.productItemStar.visibility = View.VISIBLE
        }
    }

    private fun seFavoriteIcon(
        holder: ViewHolder,
        product: ProductData
    ) {
        if (productsFragment is FavoritesFragment)
            holder.favIcon.setImageResource(R.mipmap.fav_hov_1mdpi)
        else if (product.isFavorite != null)
            holder.favIcon.setImageResource(R.mipmap.fav_hov_1mdpi)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var mCurrentPosition: Int = 0

        fun getCurrentPosition(): Int {
            return mCurrentPosition
        }

        val title: TextView = itemView.productItemTitle
        val description: TextView = itemView.productItemDescription
        val date: TextView = itemView.productItemDate
         val cost : TextView = itemView.productItemCost
        val imageView: ImageView = itemView.productItemImg
        val favIcon = itemView.productItemFav
        val deleteIcon = itemView.productItemDelete
        val editItem = itemView.productItemEdit
        val editItemDraft = itemView.productItemDraft
        val productItemStar = itemView.productItemStar

    }
}