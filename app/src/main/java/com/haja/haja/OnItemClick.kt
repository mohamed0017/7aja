package com.haja.haja

import com.haja.haja.Service.model.OfferCategoriesData

 interface OnItemClick {

    fun onClick(possion:Int, itemData : OfferCategoriesData)
}