package com.haja.haja.View.ui.MainCategoriesScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.AdsModel
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Utils.LANG
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.Utils.TOKEN
import com.haja.haja.model.CategoriesModel

class MainCategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")
    private val repository = AppRepository(token.toString())
    private var parentID = 0

    fun ssetParentId(id : Int){
        parentID = id
    }
    fun getCategories(parentId : Int?): SingleLiveEvent2<CategoriesModel> {
        return  repository.getCategories(parentId!!, "$lang")
    }

    fun getAds(parentId : Int?): SingleLiveEvent2<AdsModel> {
        return  repository.getAds(parentId!!, "$lang")
    }

    fun getStartupAd(): SingleLiveEvent2<AdsModel> {
        return  repository.getStartupAd( "$lang")
    }
}
