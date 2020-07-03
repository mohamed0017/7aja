package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class ErrorMesageEnModel(

	@field:SerializedName("name")
	val name: List<String?>? = null,

	@field:SerializedName("mobile")
	val mobile: List<String?>? = null,

	@field:SerializedName("email")
	val email: List<String?>? = null
)