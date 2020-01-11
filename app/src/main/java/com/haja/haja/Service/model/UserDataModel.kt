package com.haja.haja.Service.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDataModel(

	@field:SerializedName("date_birth")
	val dateBirth: String? = null,

	@field:SerializedName("activitation_code")
	var activitationCode: String? = null,

	@field:SerializedName("img")
	val img: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("is_confirmed")
	val isConfirmed: Int? = null,

	@field:SerializedName("latitude")
	val latitude: Int? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("user_type")
	val userType: Int? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("nationality")
	val nationality: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("longitude")
	val longitude: Int? = null
) : Parcelable