package com.haja.haja.Service.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.haja.haja.Service.ApiService
import com.haja.haja.Service.ServiceGenerator
import com.haja.haja.Service.enqueue
import com.haja.haja.Service.model.AdsModel
import com.haja.haja.Service.model.StaticPagesModel
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.model.CategoriesModel

class AppRepository private constructor() {

    companion object {
        val getInstance = AppRepository()
    }

    private var apiService: ApiService? = null

    init {
        apiService = ServiceGenerator.createService
    }

    fun getCategories(parentId:Int, language: String): SingleLiveEvent2<CategoriesModel> {
        val result = SingleLiveEvent2<CategoriesModel>()
        val call = apiService?.getCategories(parentId = parentId, lang = language)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getCategories", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getCategories/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun getAds(categoryId:Int, language: String): SingleLiveEvent2<AdsModel> {
        val result = SingleLiveEvent2<AdsModel>()
        val call = apiService?.getAds(categoryId = categoryId, lang = language)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getAds", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getAds/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun getStartupAd( language: String): SingleLiveEvent2<AdsModel> {
        val result = SingleLiveEvent2<AdsModel>()
        val call = apiService?.getStartupAd(lang = language)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getStartupAd", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getStartupAd/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun getStaticPages( type: Int): SingleLiveEvent2<StaticPagesModel> {
        val result = SingleLiveEvent2<StaticPagesModel>()
        val call = apiService?.getStaticPages(type)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getStaticPages", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getStaticPages/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }
}