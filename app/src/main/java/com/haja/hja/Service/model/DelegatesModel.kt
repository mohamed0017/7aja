package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class DelegatesModel(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("error_mesage")
	val errorMesage: String? = null,

	@field:SerializedName("error_mesage_en")
	val errorMesageEn: String? = null,

	@field:SerializedName("data")
	val delegatesdata: DelegatesdataModel? = null
)