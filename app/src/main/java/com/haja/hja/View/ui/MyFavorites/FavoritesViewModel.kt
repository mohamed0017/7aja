package com.haja.hja.View.ui.MyFavorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.hja.Service.model.FavoraitesModle
import com.haja.hja.Service.model.FavoriteModel
import com.haja.hja.Service.repository.CategoriesRepository
import com.haja.hja.Utils.LANG
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.SingleLiveEvent2
import com.haja.hja.Utils.TOKEN

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
