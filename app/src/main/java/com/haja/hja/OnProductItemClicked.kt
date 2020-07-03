package com.haja.hja

import android.widget.ImageView
import com.haja.hja.Service.model.ProductData

interface OnProductItemClicked {
    fun onClick(
        possion: Int,
        itemData: ProductData,
        favIcon: ImageView
    )
}