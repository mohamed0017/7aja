package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

data class ChatMessagesDataModel(

	@field:SerializedName("is_viewed")
	val isViewed: String? = null,

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("2onesignal_id")
	val jsonMember2onesignalId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("message")
	var message: String? = null,

	@field:SerializedName("user_id")
	var userId: String? = null,

	@field:SerializedName("userId2")
	var userId2: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	var id: Int? = null,

	@field:SerializedName("name2")
	val name2: String? = null,

	@field:SerializedName("user2_id")
	val user2Id: String? = null,

	@field:SerializedName("img2")
	val img2: String? = null
)