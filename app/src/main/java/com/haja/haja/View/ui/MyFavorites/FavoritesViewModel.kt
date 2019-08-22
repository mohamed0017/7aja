package com.haja.haja.View.ui.MyFavorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.FavoraitesModle
import com.haja.haja.Service.model.FavoriteModel
import com.haja.haja.Service.repository.CategoriesRepository
import com.haja.haja.Utils.SingleLiveEvent2

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var products: SingleLiveEvent2<FavoraitesModle>
    private val repository = CategoriesRepository.getInstance

    fun getMyFavorites(id:Int) : SingleLiveEvent2<FavoraitesModle> {
        products = repository.getFavorites(id,"ar")
        return products
    }

    fun removeFromFav(productId:Int): SingleLiveEvent2<FavoriteModel> {
        return repository.removeProductFromFavorite(productId)
    }
}
