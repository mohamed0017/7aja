package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class NotificationData(

	@field:SerializedName("with_id")
	val withId: String? = null,

	@field:SerializedName("url_link")
	val urlLink: String? = null,

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("details")
	val details: String? = null,

	@field:SerializedName("to_users")
	val toUsers: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null
)