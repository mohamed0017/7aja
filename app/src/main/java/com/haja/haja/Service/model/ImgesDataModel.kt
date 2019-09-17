package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

data class ImgesDataModel(

	@field:SerializedName("with_id")
	val withId: String? = null,

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url_l")
	val urlL: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)