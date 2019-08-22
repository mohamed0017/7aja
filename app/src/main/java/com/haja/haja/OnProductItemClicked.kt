package com.haja.haja

import android.widget.ImageView
import com.haja.haja.Service.model.ProductData

interface OnProductItemClicked {
    fun onClick(
        possion: Int,
        itemData: ProductData,
        favIcon: ImageView
    )
}