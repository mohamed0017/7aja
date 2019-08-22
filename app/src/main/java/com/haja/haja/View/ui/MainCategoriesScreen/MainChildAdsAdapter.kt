package com.haja.haja.View.ui.MainCategoriesScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.R
import com.haja.haja.Service.ApiService
import com.haja.haja.Service.model.AdsData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.ads_item.view.*
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri

class MainChildAdsAdapter (
    private val children: List<AdsData?>?,
   private val context: Context)
    : RecyclerView.Adapter<MainChildAdsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.ads_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val child = children?.get(position)
        Picasso.get().load(ApiService.IMAGEBASEURL +child?.img)
            .placeholder(context.resources.getDrawable(R.drawable.placeholder))
            .error(context.resources.getDrawable(R.drawable.placeholder)).into(holder.imageView)
        //   holder.textView.text = child?.name

        holder.imageView.setOnClickListener {
            if (child?.urlL != null && child.urlL.contains("http")){
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(child.urlL)
                context.startActivity(i)
            }
        }
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

       // val textView : TextView = itemView.childCatName
       val imageView: ImageView = itemView.childCatAdImg

    }
}