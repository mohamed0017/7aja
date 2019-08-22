package com.haja.haja.View.ui.SubCategoriesScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.R
import com.haja.haja.Utils.inTransaction
import com.haja.haja.View.ui.Products.ProductsFragment
import com.haja.haja.model.CategoriesData
import kotlinx.android.synthetic.main.sub_child_category_item.view.*

class SubCategoriesAdapter (
    private val children: List<CategoriesData?>?,
   private val fragmentManager: FragmentManager?
)
    : RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.sub_child_category_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val child = children?.get(position)
          holder.catName.text = child?.name
          holder.adsCounnt.text = child?.countProduct.toString()

        holder.itemView.setOnClickListener {
            if (child?.countProduct == 0){
                fragmentManager?.inTransaction {
                    replace(R.id.mainContainer, SubCategoriesFragment.newInstance(child.id!!))
                        .addToBackStack("products")
                }
            }else{
                fragmentManager?.inTransaction {
                    replace(R.id.mainContainer, ProductsFragment.newInstance(child?.id!!))
                        .addToBackStack("products")
                }
            }

        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
         val catName = itemView.subCategoryName
         val adsCounnt = itemView.subCategoryAdsCount
    }
}