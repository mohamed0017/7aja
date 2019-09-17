package com.haja.haja.View.ui.SubCategoriesScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.R
import com.haja.haja.Service.ApiService
import com.haja.haja.Utils.inTransaction
import com.haja.haja.View.ui.Products.ProductsFragment
import com.haja.haja.model.CategoriesData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.sub_child_category_item.view.*

class SubCategoriesAdapter(
    private val children: List<CategoriesData?>?,
    private val fragmentManager: FragmentManager?,
    private val  context: Context
) : RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.sub_child_category_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children?.size ?: 0
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val child = children?.get(position)
        holder.catName.text = child?.name
        holder.itemView.setOnClickListener {
            if (child?.countSubCat == 0) {
                fragmentManager?.inTransaction {
                    replace(R.id.mainContainer, ProductsFragment.newInstance(child.id!!, child.name))
                        .addToBackStack("ProductsFragment")
                }
            } else {
                fragmentManager?.inTransaction {
                    replace(R.id.mainContainer, SubCategoriesFragment.newInstance(child?.id!!, child.name))
                        .addToBackStack("SubCategoriesFragment")
                }
            }

        }
        Picasso.get().load(ApiService.IMAGEBASEURL +child?.img)
            .placeholder(context.resources.getDrawable(R.mipmap.note_icon2hdpi))
            .error(context.resources.getDrawable(R.mipmap.note_icon2hdpi)).into(holder.imageView)
        holder.bind(child)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.subChildCatImg
        val catName = itemView.subCategoryName
        val adsCounnt = itemView.subCategoryAdsCount
        val subCategoriesCount = itemView.subCategoriesCount
        val adsCounntLayout = itemView.subCategoryAdsCountSection
        val subCategoriesCountLayout = itemView.subCategoriesCountSection

        fun bind(child: CategoriesData?) {
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
}