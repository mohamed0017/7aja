package com.haja.hja.View.ui.MainCategoriesScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.haja.hja.R
import com.haja.hja.Service.ApiService
import com.haja.hja.Service.model.AdsData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.ads_item.view.*

class MainChildAdsAdapter(
    private val children: List<AdsData?>?,
    private val context: Context
) : RecyclerView.Adapter<MainChildAdsAdapter.AdsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdsViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.ads_item, parent, false)
        return AdsViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children?.size!!
    }

    override fun onBindViewHolder(
        holder: AdsViewHolder,
        position: Int
    ) {
        holder.bind()
    }

    inner class AdsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            // val textView : TextView = itemView.childCatName
            val imageView: ImageView = itemView.childCatAdImg
            val child = children?.get(adapterPosition)
            Picasso.get().load(ApiService.IMAGEBASEURL + child?.img)
                .placeholder(context.resources.getDrawable(R.drawable.placeholder))
                .error(context.resources.getDrawable(R.drawable.placeholder)).into(imageView)
            //   holder.textView.text = child?.name

            imageView.setOnClickListener {
                if (child?.urlL != null && child.urlL.contains("http")) {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(child.urlL)
                    context.startActivity(i)
                }
            }
        }
    }
}