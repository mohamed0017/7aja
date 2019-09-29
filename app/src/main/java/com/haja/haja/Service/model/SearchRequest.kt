package com.haja.haja.Service.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchRequest(

    @field:SerializedName("user_id")
    var userId: String? = null,

    @field:SerializedName("kw_search")
    var kwSearch: String? = null,

    @field:SerializedName("from_price")
    var fromPrice: String? = null,

    @field:SerializedName("to_price")
    var toPrice: String? = null
) : Parcelable