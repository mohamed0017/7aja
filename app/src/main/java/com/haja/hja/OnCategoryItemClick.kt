package com.haja.hja

import com.haja.hja.model.CategoriesData

interface OnCategoryItemClick {
    fun onClick(possion:Int, itemData : CategoriesData)
}