package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

data class AdPricedataModel(

	@field:SerializedName("num_free_advs")
	val numFreeAdvs: String? = null,

	@field:SerializedName("user_free_ads")
	val userFreeAds: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("price_adv")
	val priceAdv: String? = null
)