package com.haja.haja.Service

import com.haja.haja.Service.model.*
import com.haja.haja.model.CategoriesModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    companion object {
        const val BASEURL = "https://7aja.puresoft.me/"
        const val IMAGEBASEURL = "https://7aja.puresoft.me/uploads/"
    }

    @Headers("Accept: application/json")
    @GET("/api/categories/t/{lang}/1/{id}")
    fun getCategories(
        @Path("lang") lang: String,
        @Path("id") parentId: Int
    ): Call<CategoriesModel>

    @Headers("Accept: application/json")
    @GET("/api/advertising/t/{lang}/1")
    fun getAds(
        @Path("lang") lang: String,
        @Query("with_id") categoryId: Int
    ): Call<AdsModel>

    @Headers("Accept: application/json")
    @GET("/api/advertising/t/{lang}/0")
    fun getStartupAd(
        @Path("lang") lang: String
    ): Call<AdsModel>

    @Headers("Accept: application/json")
    @GET("/api/static_page/{type}")
    fun getStaticPages(
        @Path("type") type: Int
    ): Call<StaticPagesModel>

    @Headers("Accept: application/json")
    @GET("/api/products/t/{lang}/1/{id}")
    fun getProducts(
        @Path("lang") lang: String,
        @Path("id") parentId: Int
    ): Call<ProductsModel>

    @Headers("Accept: application/json")
    @GET("/api/products/t/{lang}/2/{id}")
    fun getOffers(
        @Path("lang") lang: String,
        @Path("id") parentId: Int
    ): Call<ProductsModel>

    @Headers("Accept: application/json")
    @GET("/api/categories/cats_4offers/{lang}")
    fun getOffersCategories(
        @Path("lang") lang: String
    ): Call<OffersCategoriesModel>

    @Headers("Accept: application/json")
    @POST("/api/login")
    fun login(
        @QueryMap map: HashMap<String, String>
    ): Call<UserModel>

    @Headers("Accept: application/json")
    @POST("/api/register")
    fun register(
        @QueryMap map: HashMap<String, String>
    ): Call<UserModel>

    @Headers("Accept: application/json")
    @POST("/api/user_favorite")
    fun addToFavorite(
        @Query("favo_id") productId: Int
    ): Call<FavoriteModel>

    @Headers("Accept: application/json")
    @DELETE("/api/user_favorite/{id}")
    fun removeFormFavorite(
        @Path("id") productId: Int
    ): Call<FavoriteModel>

    @Headers("Accept: application/json")
    @GET("/api/user_favorite/t/{lang}/{id}")
    fun getFavorites(
        @Path("id") id: Int,
        @Path("lang") lang: String
    ): Call<FavoraitesModle>

    @Headers("Accept: application/json")
    @GET("/api/attributes/{lang}/{id}")
    fun getCategoryAttributes(
        @Path("id") id: Int,
        @Path("lang") lang: String
    ): Call<AddProAttributesModel>

    @Multipart
    @Headers("Accept: application/json")
    @POST("/api/products")
    fun addProduct(
        @QueryMap map: HashMap<String, String>,
        @Part lang: List<MultipartBody.Part>,
        @QueryMap productAttributes: HashMap<String, List<String>>
    ): Call<AddProductResponse>
}