package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class FavoraitesModle(
    @field:SerializedName("result")
    val result: Boolean? = null,

    @field:SerializedName("error_mesage")
    val errorMesage: String? = null,

    @field:SerializedName("type")
    var type: String? = null,

    @field:SerializedName("data")
    val data: List<ProductData>? = null,

    @field:SerializedName("error_mesage_en")
    val errorMesageEn: String? = null
)