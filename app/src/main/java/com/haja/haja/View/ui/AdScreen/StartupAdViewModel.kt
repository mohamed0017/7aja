package com.haja.haja.View.ui.AdScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.AdsModel
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Utils.LANG
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.Utils.TOKEN

class StartupAdViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = AppRepository(token.toString())

    fun getStartupAd(): SingleLiveEvent2<AdsModel> {
        val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")
        return  repository.getStartupAd( "$lang")
    }
}