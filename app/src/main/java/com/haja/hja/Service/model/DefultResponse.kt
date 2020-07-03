package com.haja.hja.Service.model

import com.google.gson.annotations.SerializedName

data class DefultResponse(
    @field:SerializedName("result")
    val result: Boolean? = null,

    @field:SerializedName("insert_id")
    val insertId: Int? = null,

    @field:SerializedName("error_mesage")
    val errorMesage: String? = null,

    @field:SerializedName("error_mesage_en")
    val errorMesageEn: String? = null
)