package com.haja.haja.View.ui.MainCategoriesScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.R
import com.haja.haja.Service.ApiService
import com.haja.haja.Utils.inTransaction
import com.haja.haja.View.ui.Products.ProductsFragment
import com.haja.haja.View.ui.SubCategoriesScreen.SubCategoriesFragment
import com.haja.haja.model.CategoriesData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.child_category_item.view.*

class MainChildCatAdapter(
    private val children: List<CategoriesData?>?,
    private val context: Context,
    private val fragmentManager: FragmentManager?
)
    : RecyclerView.Adapter<MainChildCatAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.child_category_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val child = children?.get(position)
        Picasso.get().load(ApiService.IMAGEBASEURL +child?.img)
            .placeholder(context.resources.getDrawable(R.mipmap.note_icon2hdpi))
            .error(context.resources.getDrawable(R.mipmap.note_icon2hdpi)).into(holder.imageView);
        holder.textView.text = child?.name

        holder.itemView.setOnClickListener {
            if (child?.countSubCat == 0) {
                fragmentManager?.inTransaction {
                    replace(R.id.mainContainer, ProductsFragment.newInstance(child.id!!, child.name))
                        .addToBackStack("ProductsFragment")
                }
            } else {
                fragmentManager?.inTransaction {
                    replace(R.id.mainContainer, SubCategoriesFragment.newInstance(child?.id!! , child.name))
                        .addToBackStack("SubCategoriesFragment")
                }
            }
        }
    }
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textView : TextView = itemView.childCatName
        val imageView: ImageView = itemView.childCatImg

    }
}