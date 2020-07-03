package com.haja.hja.Service.repository

import android.util.Log
import com.haja.hja.Service.ApiService
import com.haja.hja.Service.ServiceGenerator
import com.haja.hja.Service.enqueue
import com.haja.hja.Service.model.OffersCategoriesModel
import com.haja.hja.Service.model.ProductsModel
import com.haja.hja.Utils.SingleLiveEvent2

class OffersRepository(token:String){

    private var apiService: ApiService? = null

    init {
        apiService = ServiceGenerator(token).createService
    }


    fun getOfferCategories(language: String): SingleLiveEvent2<OffersCategoriesModel> {
        val result = SingleLiveEvent2<OffersCategoriesModel>()
        val call = apiService?.getOffersCategories( lang = language)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getOfferCategories", response.code().toString())
                Log.i("getOfferCategories/url", call.request().url.toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("OfferCategories/Failure", t!!.message +"..")
                result.value = null
            }
        }
        return result
    }

    fun getOffers(parentId: Int, language: String, userId: String): SingleLiveEvent2<ProductsModel> {
        val result = SingleLiveEvent2<ProductsModel>()
        val call = apiService?.getOffers(parentId = parentId, lang = language, userId = userId.toInt() )
        call?.enqueue {
            onResponse = { response ->
                Log.i("getOffers", response.code().toString())
                Log.i("getOffers/url", call.request().url.toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getOffers/Failure", t!!.message +"..")
                result.value = null
            }
        }
        return result
    }
}