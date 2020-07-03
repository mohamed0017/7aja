package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class OffersCategoriesModel(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("error_mesage")
	val errorMesage: String? = null,

	@field:SerializedName("data")
	val data: List<OfferCategoriesData?>? = null,

	@field:SerializedName("error_mesage_en")
	val errorMesageEn: String? = null
)