package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

data class FaqDataModel(

	@field:SerializedName("ord")
	val ord: Int? = null,

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("details")
	val details: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)