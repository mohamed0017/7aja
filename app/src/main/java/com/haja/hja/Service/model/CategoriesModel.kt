package com.haja.hja.model

import com.google.gson.annotations.SerializedName

data class CategoriesModel(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("error_mesage")
	val errorMessage: String? = null,

	@field:SerializedName("data")
	val data: List<CategoriesData?>? = null,

	@field:SerializedName("error_mesage_en")
	val errorMessageEn: String? = null
)