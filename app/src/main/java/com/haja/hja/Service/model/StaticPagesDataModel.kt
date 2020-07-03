package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class StaticPagesDataModel(

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("details")
	val details: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("name_en")
	val nameEn: String? = null,

	@field:SerializedName("details_en")
	val detailsEn: String? = null
)