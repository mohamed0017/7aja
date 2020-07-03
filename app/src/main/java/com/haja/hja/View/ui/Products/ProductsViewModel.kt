package com.haja.hja.View.ui.Products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.hja.Service.model.FavoriteModel
import com.haja.hja.Service.model.ProductsModel
import com.haja.hja.Service.repository.CategoriesRepository
import com.haja.hja.Utils.LANG
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.SingleLiveEvent2
import com.haja.hja.Utils.TOKEN

class ProductsViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var products: SingleLiveEvent2<ProductsModel>
    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = CategoriesRepository(token.toString())
    var page = 0

    fun getProducts(categoryID: Int, userId: Int, isAll: Boolean) : SingleLiveEvent2<ProductsModel>{
        val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")
        products = repository.getProducts(categoryID,"$lang",userId,isAll, "$page")
        return products
    }

    fun addToFav(productId:Int): SingleLiveEvent2<FavoriteModel> {
        return repository.addToFavorite(productId)
    }

    fun removeFromFav(productId:Int): SingleLiveEvent2<FavoriteModel> {
        return repository.removeProductFromFavorite(productId)
    }
}
