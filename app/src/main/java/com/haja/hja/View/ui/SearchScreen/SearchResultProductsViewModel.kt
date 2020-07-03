package com.haja.hja.View.ui.SearchScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.hja.Service.model.FavoriteModel
import com.haja.hja.Service.model.ProductsModel
import com.haja.hja.Service.model.SearchRequest
import com.haja.hja.Service.repository.CategoriesRepository
import com.haja.hja.Service.repository.ProductRepository
import com.haja.hja.Utils.LANG
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.SingleLiveEvent2
import com.haja.hja.Utils.TOKEN

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
