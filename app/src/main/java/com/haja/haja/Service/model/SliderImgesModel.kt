package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

data class SliderImgesModel(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("data")
	val imgesData: List<ImgesDataModel?>? = null,

	@field:SerializedName("error_mesage")
	val errorMesage: String? = null,

	@field:SerializedName("error_mesage_en")
	val errorMesageEn: String? = null
)