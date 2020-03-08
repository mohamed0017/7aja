package com.haja.haja.model

import com.google.gson.annotations.SerializedName
import com.haja.haja.Service.model.AdsData

data class CategoriesData(

	var childCategories : List<CategoriesData?>? = null,

	var childAds : List<AdsData?>? = null,

	@field:SerializedName("ord")
	val ord: Int? = null,

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("parent_id")
	val parentId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("count_product")
	val countProduct: Int? = null,

	@field:SerializedName("count_sub_cat")
	val countSubCat: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: Int? = null

)