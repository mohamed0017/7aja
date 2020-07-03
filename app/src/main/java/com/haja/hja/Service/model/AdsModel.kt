package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class AdsModel(

    @field:SerializedName("result")
    val result: Boolean? = null,

    @field:SerializedName("error_mesage")
    val errorMessage: String? = null,

    @field:SerializedName("data")
    val data: List<AdsData?>? = null,

    @field:SerializedName("error_mesage_en")
    val errorMessageEn: String? = null
)