package com.haja.haja.View.ui.OffersScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.OffersCategoriesModel
import com.haja.haja.Service.model.ProductsModel
import com.haja.haja.Service.repository.OffersRepository
import com.haja.haja.Utils.SingleLiveEvent2

class OffersViewModel (application: Application) : AndroidViewModel(application) {

    private val repository = OffersRepository.getInstance

    fun getCategories(): SingleLiveEvent2<OffersCategoriesModel> {
        return repository.getOfferCategories("ar")
    }

    fun getOfferts(categoryId : Int): SingleLiveEvent2<ProductsModel> {
        return repository.getOffers(categoryId,"ar")
    }
}
