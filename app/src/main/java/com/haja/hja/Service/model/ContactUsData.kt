package com.haja.hja.Service.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactUsData(

	@field:SerializedName("contact_details")
	val contactDetails: ContactDetailsModel? = null,

	@field:SerializedName("social_medias")
	val socialMedias: List<SocialMediasModel?>? = null
) : Parcelable