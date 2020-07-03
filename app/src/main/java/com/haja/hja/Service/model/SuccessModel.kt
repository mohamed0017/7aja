package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class SuccessModel(

	@field:SerializedName("token")
	val token: String? = null
)