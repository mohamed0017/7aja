package com.haja.haja.View.ui.AddProduct

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.OnCategoryItemClick
import com.haja.haja.R
import com.nguyenhoanglam.imagepicker.model.Image
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.selected_img_item.view.*
import java.io.File

class SelectedImagesAdapter(
    private val context : Context)
    : RecyclerView.Adapter<SelectedImagesAdapter.ViewHolder>(){

    private var images: List<Image>? = null

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        val v =  LayoutInflater.from(parent.context)
            .inflate(R.layout.selected_img_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return if (images == null) 0
        else images?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val image = images?.get(position)

        holder.imageView.setImageURI(Uri.parse(File(image?.path).toString()))

    }

    fun setImages(categories: List<Image>){
        this.images = categories
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imageView = itemView.selectedImageItem
    }
}