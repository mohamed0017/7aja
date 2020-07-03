package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class StaticPagesModel(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("error_mesage")
	val errorMesage: String? = null,

	@field:SerializedName("data")
	val data: StaticPagesDataModel? = null,

	@field:SerializedName("error_mesage_en")
	val errorMesageEn: String? = null
)