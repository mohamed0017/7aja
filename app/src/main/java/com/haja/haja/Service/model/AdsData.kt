package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

data class AdsData(

	@field:SerializedName("with_id")
	val withId: Int? = null,

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("is_like")
	val isLike: String? = null,

	@field:SerializedName("likes")
	val likes: String? = null,

	@field:SerializedName("num_views")
	val numViews: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url_l")
	val urlL: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)