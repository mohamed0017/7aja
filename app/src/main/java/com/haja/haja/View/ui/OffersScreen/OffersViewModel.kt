package com.haja.haja.View.ui.OffersScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.OffersCategoriesModel
import com.haja.haja.Service.model.ProductsModel
import com.haja.haja.Service.repository.CategoriesRepository
import com.haja.haja.Service.repository.OffersRepository
import com.haja.haja.Utils.LANG
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.Utils.TOKEN

class OffersViewModel (application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = OffersRepository(token.toString())
    val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")

    fun getCategories(): SingleLiveEvent2<OffersCategoriesModel> {
        return repository.getOfferCategories("$lang")
    }

    fun getOfferts(categoryId : Int): SingleLiveEvent2<ProductsModel> {
        return repository.getOffers(categoryId,"$lang")
    }
}
