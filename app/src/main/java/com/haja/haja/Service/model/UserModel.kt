package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

data class UserModel(

    @field:SerializedName("result")
    val result: Boolean? = null,

    @field:SerializedName("data")
	val data: UserDataModel? = null,

    @field:SerializedName("error_mesage")
    val errorMesage: String? = null,

    @field:SerializedName("error_mesage_en")
    val errorMesageEn: String? = null
)