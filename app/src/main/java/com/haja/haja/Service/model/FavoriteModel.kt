package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

data class FavoriteModel(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("insert_id")
	val insertID: Int? = null,

	@field:SerializedName("error_mesage")
	val errorMesage: String? = null,
	
	@field:SerializedName("error_mesage_en")
	val errorMesageEn: String? = null
)