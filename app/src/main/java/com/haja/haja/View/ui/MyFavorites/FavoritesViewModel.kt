package com.haja.haja.View.ui.MyFavorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.FavoraitesModle
import com.haja.haja.Service.model.FavoriteModel
import com.haja.haja.Service.repository.CategoriesRepository
import com.haja.haja.Utils.LANG
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.Utils.TOKEN

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = CategoriesRepository(token.toString())
    private lateinit var products: SingleLiveEvent2<FavoraitesModle>

    fun getMyFavorites(id:Int) : SingleLiveEvent2<FavoraitesModle> {
        val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")
        products = repository.getFavorites(id,"$lang")
        return products
    }

    fun removeFromFav(productId:Int): SingleLiveEvent2<FavoriteModel> {
        return repository.removeProductFromFavorite(productId)
    }
}
