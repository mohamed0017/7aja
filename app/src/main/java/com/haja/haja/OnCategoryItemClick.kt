package com.haja.haja

import com.haja.haja.model.CategoriesData

interface OnCategoryItemClick {
    fun onClick(possion:Int, itemData : CategoriesData)
}