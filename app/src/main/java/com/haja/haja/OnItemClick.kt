package com.haja.haja

import com.haja.haja.Service.model.OfferCategoriesData

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