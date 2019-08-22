package com.haja.haja.Service.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductImgs(

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("img_thumbnail")
	val imgThumbnail: String? = null,

	@field:SerializedName("id_img")
	val idImg: Int? = null,

	@field:SerializedName("width")
	val width: Float? = null,

	@field:SerializedName("height")
	val height: Float? = null
) : Parcelable