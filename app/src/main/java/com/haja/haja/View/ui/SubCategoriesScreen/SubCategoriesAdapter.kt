package com.haja.haja.View.ui.SubCategoriesScreen

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.R
import com.haja.haja.Service.ApiService
import com.haja.haja.Utils.inTransaction
import com.haja.haja.View.ui.MainCategoriesScreen.MainChildAdsAdapter
import com.haja.haja.View.ui.Products.ProductsFragment
import com.haja.haja.model.CategoriesData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.sub_child_ads_item.view.*
import kotlinx.android.synthetic.main.sub_child_category_item.view.*

const val AD_ITEM_TYPE = 2

class SubCategoriesAdapter(
    private val children: List<CategoriesData?>?,
    private val fragmentManager: FragmentManager?,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return if (viewType == AD_ITEM_TYPE) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.sub_child_ads_item, parent, false)
            AdsViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.sub_child_category_item, parent, false)
            ViewHolder(v)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val isHaveAds = !children?.get(position)?.childAds.isNullOrEmpty()
        Log.e("isHaveAds:  $position", isHaveAds.toString())
        return if (position == 6 && isHaveAds) AD_ITEM_TYPE else if (position == 13 && isHaveAds) AD_ITEM_TYPE
        else if (position == 20 && isHaveAds) AD_ITEM_TYPE else 0
    }

    override fun getItemCount(): Int {
        return children?.size ?: 0
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (getItemViewType(position) == AD_ITEM_TYPE){
            val vieHolder = holder as AdsViewHolder
            vieHolder.bind()
        }else{
            val vieHolder = holder as ViewHolder
            vieHolder.bind()
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.subChildCatImg
        val catName = itemView.subCategoryName
        val adsCounnt = itemView.subCategoryAdsCount
        val subCategoriesCount = itemView.subCategoriesCount
        val adsCounntLayout = itemView.subCategoryAdsCountSection
        val subCategoriesCountLayout = itemView.subCategoriesCountSection

        fun bind() {
            val child = children?.get(adapterPosition)
            catName.text = child?.name
            itemView.setOnClickListener {
                if (child?.countSubCat == 0) {
                    fragmentManager?.inTransaction {
                        replace(
                            R.id.mainContainer,
                            ProductsFragment.newInstance(child.id!!, child.name)
                        )
                            .addToBackStack("ProductsFragment")
                    }
                } else {
                    fragmentManager?.inTransaction {
                        replace(
                            R.id.mainContainer,
                            SubCategoriesFragment.newInstance(child?.id!!, child.name)
                        )
                            .addToBackStack("SubCategoriesFragment")
                    }
                }

            }

            Picasso.get().load(ApiService.IMAGEBASEURL + child?.img)
                .placeholder(context.resources.getDrawable(R.mipmap.note_icon2hdpi))
                .error(context.resources.getDrawable(R.mipmap.note_icon2hdpi)).into(imageView)

            if (child?.countSubCat == 0) {
                adsCounntLayout.visibility = View.VISIBLE
                subCategoriesCountLayout.visibility = View.GONE
                adsCounnt.text = child.countProduct.toString()
            } else {
                subCategoriesCountLayout.visibility = View.VISIBLE
                adsCounntLayout.visibility = View.GONE
                subCategoriesCount.text = child?.countSubCat.toString()
            }
        }
    }


    inner class AdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            val adapter = MainChildAdsAdapter(children?.get(adapterPosition)?.childAds, context)
            itemView.subCategoriesAds.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            itemView.subCategoriesAds.adapter = adapter
            autoScrool(itemView.subCategoriesAds, adapter)
            itemView.subCategoriesAds.setRecycledViewPool(viewPool)
        }
    }
    fun autoScrool(
        holder: RecyclerView,
        adapter: MainChildAdsAdapter
    ) {
        val speedScroll = 3000
        val handler = Handler()
        val runnable = object : Runnable {
            var count = 0
            var flag = true
            override fun run() {
                if (count < adapter.itemCount) {
                    if (count == adapter.itemCount - 1) {
                        flag = false
                    } else if (count == 0) {
                        flag = true
                    }
                    if (flag)
                        count++
                    else
                        count--

                    holder.smoothScrollToPosition(count)
                    handler.postDelayed(this, speedScroll.toLong())
                }
            }
        }

        handler.postDelayed(runnable, speedScroll.toLong())
    }
}