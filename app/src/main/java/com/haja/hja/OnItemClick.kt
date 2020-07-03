package com.haja.hja

import com.haja.hja.Service.model.OfferCategoriesData

 interface OnItemClick {

    fun onClick(
        itemData: OfferCategoriesData,
        position: Int
    )
}
interface OnItemClickWithId{
     fun onClick(
        id: Int,
        position: Int
    )
}