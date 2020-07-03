package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class ChatsModel(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("data")
	val chatsData: List<ChatsDataModel?>? = null,

	@field:SerializedName("error_mesage")
	val errorMesage: String? = null,

	@field:SerializedName("error_mesage_en")
	val errorMesageEn: String? = null
)