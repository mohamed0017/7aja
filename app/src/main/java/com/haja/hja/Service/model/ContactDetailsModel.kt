package com.haja.hja.Service.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactDetailsModel(

	@field:SerializedName("result")
	val result: Boolean? = null,

	@field:SerializedName("error_mesage")
	val errorMesage: String? = null,

	@field:SerializedName("data")
	val data: ContactUsData? = null,

	@field:SerializedName("error_mesage_en")
	val errorMesageEn: String? = null,

	@field:SerializedName("mobile2")
	val mobile2: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("tel")
	val tel: String? = null,

	@field:SerializedName("name_en")
	val nameEn: String? = null
) : Parcelable