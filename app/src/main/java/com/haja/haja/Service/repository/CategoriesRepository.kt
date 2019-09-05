package com.haja.haja.Service.repository

import android.util.Log
import com.haja.haja.Service.ApiService
import com.haja.haja.Service.ServiceGenerator
import com.haja.haja.Service.enqueue
import com.haja.haja.Service.model.FavoraitesModle
import com.haja.haja.Service.model.FavoriteModel
import com.haja.haja.Service.model.ProductsModel
import com.haja.haja.Utils.SingleLiveEvent2

class CategoriesRepository(token:String){

    private var apiService: ApiService? = null

    init {
        apiService = ServiceGenerator(token).createService
    }

    fun getProducts(parentId:Int, language: String ,userId : Int): SingleLiveEvent2<ProductsModel> {
        val result = SingleLiveEvent2<ProductsModel>()
        val call = apiService?.getProducts(parentId = parentId, lang = language , userId = userId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getProducts", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getProducts/Failure", t!!.message +"..")
                result.value = null
            }
        }
        return result
    }

    fun addToFavorite(productId:Int): SingleLiveEvent2<FavoriteModel> {
        val result = SingleLiveEvent2<FavoriteModel>()
        val call = apiService?. addToFavorite( productId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("addToFavorite", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("RemoveFavorite/Failure", t!!.message +"..")
                result.value = null
            }
        }
        return result
    }


    fun removeProductFromFavorite(productId:Int): SingleLiveEvent2<FavoriteModel> {
        val result = SingleLiveEvent2<FavoriteModel>()
        val call = apiService?.removeFormFavorite(productId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("removeFromFavorite", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("removeFavorite/Failure", t!!.message +"..")
                result.value = null
            }
        }
        return result
    }

    fun getFavorites(id:Int,language: String): SingleLiveEvent2<FavoraitesModle> {
        val result = SingleLiveEvent2<FavoraitesModle>()
        val call = apiService?.getFavorites(id,language)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getFavorites", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getFavorites/Failure", t!!.message +"..")
                result.value = null
            }
        }
        return result
    }
}