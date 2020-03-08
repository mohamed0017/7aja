package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

data class AdPricedataModel(

	@field:SerializedName("num_free_advs")
	val numFreeAdvs: String? = null,

	@field:SerializedName("user_free_ads")
	val userFreeAds: Int? = null,

	@field:SerializedName("special_4_12h")
	val special_4_12h: Int? = null,

	@field:SerializedName("special_4_1day")
	val special_4_1day: Int? = null,

	@field:SerializedName("special_4_2day")
	val special_4_2day: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("price_adv")
	val priceAdv: String? = null
)