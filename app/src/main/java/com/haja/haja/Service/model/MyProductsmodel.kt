package com.haja.haja.Service.model

import com.google.gson.annotations.SerializedName

class MyProductsmodel(
    @field:SerializedName("result")
    val result: Boolean? = null,

    @field:SerializedName("error_mesage")
    val errorMesage: String? = null,

    @field:SerializedName("data")
    val data: List<ProductData>? = null,

    @field:SerializedName("error_mesage_en")
    val errorMesageEn: String? = null
)