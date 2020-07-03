package com.haja.hja.View.ui.AddProduct

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haja.hja.R
import com.nguyenhoanglam.imagepicker.model.Image
import kotlinx.android.synthetic.main.selected_img_item.view.*

class SelectedImagesAdapter(
    private val context: Context
) : RecyclerView.Adapter<SelectedImagesAdapter.ViewHolder>() {

    private var images: List<Image>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.selected_img_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return if (images == null) 0
        else images?.size!!
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val image = images?.get(position)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Glide.with(context)
                .load(image?.uri)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imageView)
            //  holder.imageView.setImageURI(image?.uri)
        } else {
            Glide.with(context)
                .load(image?.path)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imageView)
            //  holder.imageView.setImageURI(Uri.parse(File(image?.path).toString()))
        }

    }

    fun setImages(categories: List<Image>) {
        this.images = categories
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.selectedImageItem
    }
}