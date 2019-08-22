package com.haja.haja.View.ui.AddProduct

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.OnCategoryItemClick
import com.haja.haja.R
import com.haja.haja.model.CategoriesData
import kotlinx.android.synthetic.main.add_product_category_item.view.*

class AddProductCatAdapter(
    private val onItemClick: OnCategoryItemClick?
)
    : RecyclerView.Adapter<AddProductCatAdapter.ViewHolder>(){

    private var children: List<CategoriesData>? = null

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.add_product_category_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return if (children == null) 0
        else children?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val category = children?.get(position)
        holder.catName.text = category?.name
        if (category?.countSubCat == 0){
            holder.itemView.nextCategory.visibility = View.INVISIBLE
        }
        holder.itemView.setOnClickListener {
            if (category != null) {
                onItemClick?.onClick(position ,  category )
            }
        }
    }

    fun setCategoriesList(categories: List<CategoriesData>){
        this.children = categories
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val catName = itemView.addProductCategoryName
    }
}