package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

data class ErrorMesageModel(

	@field:SerializedName("name")
	val name: List<String?>? = null,

	@field:SerializedName("mobile")
	val mobile: List<String?>? = null,

	@field:SerializedName("email")
	val email: List<String?>? = null
)