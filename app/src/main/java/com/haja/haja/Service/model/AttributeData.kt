package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

data class AttributeData(

	@field:SerializedName("type_inserting")
	val typeInserting: Any? = null,

	@field:SerializedName("cat_id")
	val catId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("value")
	var value: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)