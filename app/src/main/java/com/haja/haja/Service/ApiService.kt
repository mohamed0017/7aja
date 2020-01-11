package com.haja.haja.Service

import com.haja.haja.Service.model.*
import com.haja.haja.model.CategoriesModel
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    companion object {
        const val BASEURL = "https://7ajaq8.com/"
        const val IMAGEBASEURL = "https://7ajaq8.com/uploads/"
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
        @Path("lang") lang: String,
        @Query("user_id") userId: Int
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
        @Path("id") parentId: Int,
        @Query("user_id") userId: Int
    ): Call<ProductsModel>

    @Headers("Accept: application/json")
    @POST("/api/products/search/{lang}/1")
    fun searchProducts(
        @Path("lang") lang: String,
        @Body searchData: SearchRequest
    ): Call<ProductsModel>

    @Headers("Accept: application/json")
    @GET("/api/products/{id}")
    fun getSingleProduct(
        @Path("id") orderId: Int,
        @Query("lang") lang: String,
        @Query("user_id") userId: Int
    ): Call<ProductsModel>

    @Headers("Accept: application/json")
    @GET("/api/products/t/{lang}/2/{id}")
    fun getOffers(
        @Path("lang") lang: String,
        @Path("id") parentId: Int,
        @Query("user_id") userId: Int
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
        @QueryMap productAttributes: HashMap<String, String>
    ): Call<AddProductResponse>

    @Headers("Accept: application/json")
    @POST("/api/products_report")
    fun productReport(@QueryMap map: HashMap<String, String>): Call<ProductReportResponse>

    @Headers("Accept: application/json")
    @GET("/api/products/num_views/{id}")
    fun productView(@Path("id") id: Int): Call<DefultResponse>

    @Headers("Accept: application/json")
    @GET("/api/advertising/num_views/{id}")
    fun advertisingView(@Path("id") id: Int): Call<DefultResponse>

    @Headers("Accept: application/json")
    @GET("/api/notifications/t/ar/0/{id}/0")
    fun getNotifications(@Path("id") id: Int): Call<NotificationsResponse>

    @Headers("Accept: application/json")
    @GET("/api/user-products/t/1/{id}")
    fun getMyProducts(@Path("id") userId: Int): Call<ProductsModel>

    @Headers("Accept: application/json")
    @DELETE("/api/products/{id}")
    fun removeProduct(@Path("id") productId: Int): Call<DefultResponse>

    @Headers("Accept: application/json")
    @POST("/api/contact_us")
    fun contactUS(@Body contactUsModel: ContactUsModel): Call<DefultResponse>

    @Headers("Accept: application/json")
    @GET("/api/users/t/1")
    fun getDelegates(): Call<DelegatesModel>

    @Headers("Accept: application/json")
    @GET("/api/contact-details")
    fun getContactDetails(): Call<ContactDetailsModel>

    @Headers("Accept: application/json")
    @GET("/api/advertising/t/ar/3")
    fun getMainSliderImages(@Query("user_id") userId: Int): Call<SliderImgesModel>

    // chat
    @Headers("Accept: application/json")
    @POST("/api/chat_users")
    fun addChatMessage(@QueryMap messageMap: HashMap<String, String>): Call<DefultResponse>

    @Headers("Accept: application/json")
    @GET("/api/chat_users/get/users")
    fun getAllChats(): Call<ChatsModel>

    @Headers("Accept: application/json")
    @GET("/api/chat_users/t/{user_id}")
    fun getUserChat(@Path("user_id") userId: Int): Call<UserChatModel>

    @Headers("Accept: application/json")
    @DELETE("/api/chat_users/{message_id}")
    fun deleteChat(@Path("message_id") messageId: Int): Call<DefultResponse>

    @Headers("Accept: application/json")
    @POST("/api/user_likes")
    fun likeAd(@Query("type") type: Int,
               @Query("like_id") adId: Int): Call<DefultResponse>

    @Headers("Accept: application/json")
    @GET("/api/setting_app/1")
    fun getAdPrice(): Call<AdPriceModel>

    @GET("/API/send/?username=hajakw&password=Zx123Zx123&sender=hajaq8-MESSAGE&lang=3")
    fun sendSms(@Query("mobile") mobile : String,
                @Query("message") message : String): Call<ResponseBody>

    @Headers("Accept: application/json")
    @POST("/api/activate_account")
    fun accountActivation(@Query("activitation_code") code : String,
                          @Header("Authorization") token: String): Call<DefultResponse>

    @Headers("Accept: application/json")
    @POST("/api/forget_pass_user")
    fun forgetPass(@Query("mobile") mobile : String): Call<DefultResponse>

}