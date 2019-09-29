package com.haja.haja.View.ui.SearchScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.FavoriteModel
import com.haja.haja.Service.model.ProductsModel
import com.haja.haja.Service.model.SearchRequest
import com.haja.haja.Service.repository.CategoriesRepository
import com.haja.haja.Service.repository.ProductRepository
import com.haja.haja.Utils.LANG
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.Utils.TOKEN

class SearchResultProductsViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val productRepository = ProductRepository(token.toString())
    private val repository = CategoriesRepository(token.toString())

    fun searchProducts(searchData : SearchRequest): SingleLiveEvent2<ProductsModel> {
         val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")
        return productRepository.searchProducts(lang.toString(),searchData)
    }

    fun addToFav(productId:Int): SingleLiveEvent2<FavoriteModel> {
        return repository.addToFavorite(productId)
    }

    fun removeFromFav(productId:Int): SingleLiveEvent2<FavoriteModel> {
        return repository.removeProductFromFavorite(productId)
    }
}
