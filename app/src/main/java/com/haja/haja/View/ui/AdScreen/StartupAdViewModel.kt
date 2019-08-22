package com.haja.haja.View.ui.AdScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.AdsModel
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Utils.SingleLiveEvent2

class StartupAdViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var ads: SingleLiveEvent2<AdsModel>
    private val repository = AppRepository.getInstance

    fun getStartupAd(): SingleLiveEvent2<AdsModel> {
        return  repository.getStartupAd( "ar")
    }
}