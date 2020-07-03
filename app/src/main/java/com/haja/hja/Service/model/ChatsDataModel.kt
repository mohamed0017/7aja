package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class ChatsDataModel(

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("is_viewed")
	val isViewed: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("count_chat")
	val countChat: Int? = null
)