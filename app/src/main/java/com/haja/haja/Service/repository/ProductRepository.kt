package com.haja.haja.Service.repository

import android.util.Log
import com.haja.haja.Service.ApiService
import com.haja.haja.Service.ServiceGenerator
import com.haja.haja.Service.enqueue
import com.haja.haja.Service.model.AddProAttributesModel
import com.haja.haja.Service.model.AddProductResponse
import com.haja.haja.Utils.SingleLiveEvent2
import okhttp3.MultipartBody

class ProductRepository  private constructor(){

    companion object {
        val getInstance = ProductRepository()
    }

    private var apiService: ApiService? = null

    init {
        apiService = ServiceGenerator.createService
    }

    fun getCategoryAttributes(categoryId:Int, language: String): SingleLiveEvent2<AddProAttributesModel> {
        val result = SingleLiveEvent2<AddProAttributesModel>()
        val call = apiService?.getCategoryAttributes(categoryId , lang = language)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getCategoryAttributes", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("catAttributes/Failure", t!!.message +"..")
                result.value = null
            }
        }
        return result
    }


    fun addProduct(
        map: HashMap<String, String>,
        parts: List<MultipartBody.Part>,
        productAttributes: HashMap<String, List<String>>
    ): SingleLiveEvent2<AddProductResponse> {
        val result = SingleLiveEvent2<AddProductResponse>()
        val call = apiService?.addProduct(map , parts, productAttributes)
        call?.enqueue {
            onResponse = { response ->
                Log.i("AddProductResponse", response.code().toString())
                if (response.code() / 100 == 2)
                {
                    result.value = response.body()
                    Log.i("AddProductResponse/body", response.body().toString())
                }
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("addProResponse/Failure", t!!.message +"..")
                result.value = null
            }
        }
        return result
    }
}