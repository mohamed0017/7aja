package com.haja.haja.View.ui.EditProduct

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.R
import com.haja.haja.Service.ApiService.Companion.BASEURL
import com.haja.haja.Service.ApiService.Companion.IMAGEBASEURL
import com.haja.haja.Service.model.ProductImgs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.edit_product_img_item.view.*

class EditProductImagesAdapter (private val onDeleteImage: OnDeleteImage)
    : RecyclerView.Adapter<EditProductImagesAdapter.ViewHolder>(){

    private var images: ArrayList<ProductImgs?>? = null

    fun setImagesAndNotifyList(images: ArrayList<ProductImgs?>?){
        this.images = images
    }

    fun removeImageAndNotifyList(position: ProductImgs){
        images?.remove(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.edit_product_img_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return if (images == null) 0
        else images?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val img = images?.get(position)
        Picasso.get().load(IMAGEBASEURL+img?.img).placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder).into(holder.editImg)
        holder.editImgDelete.setOnClickListener {
            onDeleteImage.onDeleteImage(img)
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val editImg = itemView.editImg
        val editImgDelete = itemView.editImgDelete
    }
}