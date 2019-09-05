package com.haja.haja.View.ui.Products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.FavoriteModel
import com.haja.haja.Service.model.ProductsModel
import com.haja.haja.Service.repository.CategoriesRepository
import com.haja.haja.Utils.LANG
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.Utils.TOKEN

class ProductsViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var products: SingleLiveEvent2<ProductsModel>
    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = CategoriesRepository(token.toString())

    fun getProducts(categoryID : Int, userId : Int) : SingleLiveEvent2<ProductsModel>{
        val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")
        products = repository.getProducts(categoryID,"$lang",userId)
        return products
    }

    fun addToFav(productId:Int): SingleLiveEvent2<FavoriteModel> {
        return repository.addToFavorite(productId)
    }

    fun removeFromFav(productId:Int): SingleLiveEvent2<FavoriteModel> {
        return repository.removeProductFromFavorite(productId)
    }
}
