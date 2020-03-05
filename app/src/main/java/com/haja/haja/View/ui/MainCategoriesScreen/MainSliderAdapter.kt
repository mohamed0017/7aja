package com.haja.haja.View.ui.MainCategoriesScreen

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.haja.haja.R
import com.haja.haja.Service.ApiService
import com.haja.haja.Service.model.ImgesDataModel
import com.haja.haja.Service.model.ProductImgs
import com.haja.haja.Service.model.SliderImgesModel
import com.haja.haja.View.ui.AdScreen.AdActivity
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso

class MainSliderAdapter(private val context: Context, private val images : List<ImgesDataModel>)
    : SliderViewAdapter<MainSliderAdapter.MainSliderAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup): MainSliderAdapterVH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout_item, null)
        return MainSliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: MainSliderAdapterVH, position: Int) {
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context, AdActivity::class.java)
            intent.putExtra("offerId", images[position].id.toString())
            intent.putExtra("offerLikes", images[position].likes)
            intent.putExtra("offerName", images[position].name)
            intent.putExtra("numViews",  images[position].numViews)
            intent.putExtra("offerImage", images[position].img)
            intent.putExtra("isLike", images[position].isLike)
            intent.putExtra("mobile", images[position].mobile)
            context.startActivity(intent)
        }
        if (!images.isEmpty()) {
            Picasso.get().load(ApiService.IMAGEBASEURL + images[position].img)
                .placeholder(context.resources.getDrawable(R.drawable.placeholder))
                .error(context.resources.getDrawable(R.drawable.placeholder)).into(viewHolder.imageViewBackground)
        }
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return images.size
    }

    inner class MainSliderAdapterVH(var itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        var imageViewBackground: ImageView

        init {
            imageViewBackground = itemView.findViewById(R.id.imgSlider)
        }// textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
    }
}