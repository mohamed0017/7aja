package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

data class ContactUsModel(

	@field:SerializedName("subject")
	var subject: String? = null,

	@field:SerializedName("name")
    var name: String? = null,

	@field:SerializedName("mobile")
	var mobile: String? = null,

	@field:SerializedName("message")
	var message: String? = null
)