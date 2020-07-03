package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class NotificationsResponse(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("error_mesage")
	val errorMesage: String? = null,

	@field:SerializedName("data")
	val data: List<NotificationData?>? = null,

	@field:SerializedName("error_mesage_en")
	val errorMesageEn: String? = null
)