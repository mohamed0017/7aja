package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class OfferCategoriesData(

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("parent_id")
	val parentId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)