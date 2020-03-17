package com.haja.haja.Service.model

import android.os.Parcelable
import android.service.autofill.UserData
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductData(

	@field:SerializedName("first_page_url")
	val firstPageUrl: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("per_page")
	val perPage: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val data: List<ProductData?>? = null,

	@field:SerializedName("last_page")
	val lastPage: Int? = null,

	@field:SerializedName("last_page_url")
	val lastPageUrl: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("from")
	val from: Int? = null,

	@field:SerializedName("to")
	val to: Int? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("prev_page_url")
	val prevPageUrl: String? = null,

	@field:SerializedName("is_published")
	val isPublished: String? = null,

	@field:SerializedName("is_special")
	val isSpecial: String? = null,

	@field:SerializedName("whatsapp")
	val whatsapp: String? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null,

	@field:SerializedName("num_views")
    var numViews: Int? = null,

	@field:SerializedName("user")
	var user: UserDataModel? = null,

	@field:SerializedName("imgs")
	val imgs: List<ProductImgs?>? = null,

	@field:SerializedName("cat_name")
	val catName: String? = null,

	@field:SerializedName("cat_id")
	val catId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("likes")
	val likes: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("is_favorite")
	var isFavorite: Int? = null,

	@field:SerializedName("is_like")
	val isLike: String? = null,

	@field:SerializedName("attributes")
	val attributes: List<AttributesModel?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("product_code")
	val productCode: String? = null,

	@field:SerializedName("cat_name_en")
	val catNameEn: String? = null,

	@field:SerializedName("tags")
	val tags: String? = null,

	@field:SerializedName("city_id")
	val cityId: Int? = null
) : Parcelable