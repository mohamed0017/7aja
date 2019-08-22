package com.haja.haja.View.ui.Products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.FavoriteModel
import com.haja.haja.Service.model.ProductsModel
import com.haja.haja.Service.repository.CategoriesRepository
import com.haja.haja.Utils.SingleLiveEvent2

class ProductsViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var products: SingleLiveEvent2<ProductsModel>
    private val repository = CategoriesRepository.getInstance

    fun getProducts(categoryID : Int) : SingleLiveEvent2<ProductsModel>{
        products = repository.getProducts(categoryID,"ar")
        return products
    }

    fun addToFav(productId:Int): SingleLiveEvent2<FavoriteModel> {
        return repository.addToFavorite(productId)
    }

    fun removeFromFav(productId:Int): SingleLiveEvent2<FavoriteModel> {
        return repository.removeProductFromFavorite(productId)
    }
}
