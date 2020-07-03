package com.haja.hja.View.ui.AdScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.hja.Service.model.AdsModel
import com.haja.hja.Service.model.DefultResponse
import com.haja.hja.Service.repository.AppRepository
import com.haja.hja.Service.repository.ProductRepository
import com.haja.hja.Utils.*

class StartupAdViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = AppRepository(token.toString())
    private val productRepository = ProductRepository(token.toString())

    fun getStartupAd(): SingleLiveEvent2<AdsModel> {
        val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")
        val userId = SharedPreferenceUtil(getApplication()).getString(USERID, "0")
        return  repository.getStartupAd( "$lang",userId!!.toInt())
    }

    fun likeAdvertisement(type: Int,adId : Int): SingleLiveEvent2<DefultResponse> {
        return  productRepository.likeAdvertisement(type, adId)
    }

    fun incrementAdvertisementViews(adId : Int): SingleLiveEvent2<DefultResponse> {
        return  productRepository.advertisingView(adId)
    }

    fun incrementOfferViews(adId : Int): SingleLiveEvent2<DefultResponse> {
        return  productRepository.productView(adId)
    }
}