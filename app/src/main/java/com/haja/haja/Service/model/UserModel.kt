package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

data class UserModel(

    @field:SerializedName("result")
    val result: Boolean? = null,

    @field:SerializedName("data")
	val data: UserDataModel? = null,

    @field:SerializedName("success")
	val success: SuccessModel? = null,

    @field:SerializedName("error_mesage")
    val errorMesage: ErrorMesageModel? = null,

    @field:SerializedName("error_mesage_en")
    val errorMesageEn: ErrorMesageEnModel? = null
)