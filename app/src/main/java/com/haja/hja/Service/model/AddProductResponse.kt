package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class AddProductResponse(
    @field:SerializedName("result")
    val result: Boolean? = false,

    @field:SerializedName("error_mesage")
    val errorMesage: String? = null,

    @field:SerializedName("data")
    val data: ProductData? = null,

    @field:SerializedName("error_mesage_en")
    val errorMesageEn: String? = null
)