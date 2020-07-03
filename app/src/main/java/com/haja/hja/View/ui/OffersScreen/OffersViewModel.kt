package com.haja.hja.View.ui.OffersScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.hja.Service.model.OffersCategoriesModel
import com.haja.hja.Service.model.ProductsModel
import com.haja.hja.Service.repository.OffersRepository
import com.haja.hja.Utils.*

class OffersViewModel (application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = OffersRepository(token.toString())
    val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")

    fun getCategories(): SingleLiveEvent2<OffersCategoriesModel> {
        return repository.getOfferCategories("$lang")
    }

    fun getOfferts(categoryId : Int): SingleLiveEvent2<ProductsModel> {
         val userId = SharedPreferenceUtil(getApplication()).getString(USERID, "0")
        return repository.getOffers(categoryId,"$lang", userId.toString())
    }
}
