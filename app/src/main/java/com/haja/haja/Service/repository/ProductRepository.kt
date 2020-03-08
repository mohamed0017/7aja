package com.haja.haja.Service.repository

import android.util.Log
import com.haja.haja.Service.ApiService
import com.haja.haja.Service.ServiceGenerator
import com.haja.haja.Service.enqueue
import com.haja.haja.Service.model.*
import com.haja.haja.Utils.SingleLiveEvent2
import okhttp3.MultipartBody

class ProductRepository(token: String) {

    private var apiService: ApiService? = null

    init {
        apiService = ServiceGenerator(token).createService
    }

    fun getCategoryAttributes(
        categoryId: Int,
        language: String
    ): SingleLiveEvent2<AddProAttributesModel> {
        val result = SingleLiveEvent2<AddProAttributesModel>()
        val call = apiService?.getCategoryAttributes(categoryId, lang = language)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getCategoryAttributes", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("catAttributes/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result
    }

    fun productView(productId: Int): SingleLiveEvent2<DefultResponse> {
        val result = SingleLiveEvent2<DefultResponse>()
        val call = apiService?.productView(productId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("productView", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("productView/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result
    }


    fun advertisingView(productId: Int): SingleLiveEvent2<DefultResponse> {
        val result = SingleLiveEvent2<DefultResponse>()
        val call = apiService?.advertisingView(productId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("advertisingView", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("advertisingView/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result
    }

    fun addProduct(
        map: HashMap<String, String>,
        parts: List<MultipartBody.Part>,
        productAttributes: HashMap<String, String>
    ): SingleLiveEvent2<AddProductResponse> {
        val result = SingleLiveEvent2<AddProductResponse>()
        val call = apiService?.addProduct(map, parts, productAttributes)
        call?.enqueue {
            onResponse = { response ->
                Log.i("AddProductResponse", response.code().toString())
                if (response.code() / 100 == 2) {
                    result.value = response.body()
                    Log.i("AddProductResponse/body", response.body().toString())
                } else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("addProResponse/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result
    }

    fun editProduct(
        productId: Int,
        map: HashMap<String, String>,
        parts: List<MultipartBody.Part>?,
        productAttributes: HashMap<String, String>
    ): SingleLiveEvent2<AddProductResponse> {
        val result = SingleLiveEvent2<AddProductResponse>()
        val call =
            if (parts != null) apiService?.editProduct(productId, map, parts, productAttributes)
            else apiService?.editProduct(productId, map, productAttributes)
        call?.enqueue {
            onResponse = { response ->
                Log.i("editProduct", response.code().toString())
                if (response.code() / 100 == 2) {
                    result.value = response.body()
                    Log.i("editProduct/body", response.body().toString())
                } else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("editProduct/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result
    }


    fun productReport(map: HashMap<String, String>): SingleLiveEvent2<ProductReportResponse> {
        val result = SingleLiveEvent2<ProductReportResponse>()
        val call = apiService?.productReport(map)
        call?.enqueue {
            onResponse = { response ->
                Log.i("productReport", response.code().toString())
                if (response.code() / 100 == 2) {
                    result.value = response.body()
                    Log.i("productReport/body", response.body().toString())
                } else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("productReport/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result
    }

    fun getSingleProduct(
        orderId: Int,
        language: String,
        userId: Int
    ): SingleLiveEvent2<ProductsModel> {
        val result = SingleLiveEvent2<ProductsModel>()
        val call = apiService?.getSingleProduct(orderId = orderId, lang = language, userId = userId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getSingleProduct", response.code().toString())
                Log.i("getSingleProduct/url", call.request().url.toString())
                if (response.code() / 100 == 2) {
                    Log.i("getSingleProduct", response.body()?.errorMesage.toString())
                    result.value = response.body()
                } else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("SingleProduct/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result
    }

    fun deleteProductImg(productId: Int, imageId: Int): SingleLiveEvent2<DefultResponse> {
        val result = SingleLiveEvent2<DefultResponse>()
        val call = apiService?.deleteProductImg(productId, imageId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("deleteProductImg", response.code().toString())
                Log.i("deleteProductImg/url", call.request().url.toString())
                if (response.code() / 100 == 2) {
                    Log.i("deleteProductImg", response.body()?.errorMesage.toString())
                    result.value = response.body()
                } else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("deleteProductImg/Failur", t!!.message + "..")
                result.value = null
            }
        }
        return result
    }

    fun getMyProducts(userId: Int): SingleLiveEvent2<ProductsModel> {
        val result = SingleLiveEvent2<ProductsModel>()
        val call = apiService?.getMyProducts(userId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getMyProducts", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getMyProducts/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result
    }

    fun removeProduct(productId: Int): SingleLiveEvent2<DefultResponse> {
        val result = SingleLiveEvent2<DefultResponse>()
        val call = apiService?.removeProduct(productId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("removeProduct", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("removeProduct/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result

    }

    fun likeAdvertisement(type: Int, productId: Int): SingleLiveEvent2<DefultResponse> {
        val result = SingleLiveEvent2<DefultResponse>()
        val call = apiService?.likeAd(type, productId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("likeAdvertisement", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("likeAdv/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result
    }

    fun searchProducts(language: String, search: SearchRequest): SingleLiveEvent2<ProductsModel> {
        val result = SingleLiveEvent2<ProductsModel>()
        val call = apiService?.searchProducts(lang = language, searchData = search)
        call?.enqueue {
            onResponse = { response ->
                Log.i("searchProducts/url", call.request().url.toString())
                Log.i("searchProducts", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("searchProducts/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result
    }

    fun getAdPrice(userId: String): SingleLiveEvent2<AdPriceModel> {
        val result = SingleLiveEvent2<AdPriceModel>()
        val call = apiService?.getAdPrice(userId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getAdPrice/url", call.request().url.toString())
                Log.i("getAdPrice", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getAdPrice/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result
    }
}