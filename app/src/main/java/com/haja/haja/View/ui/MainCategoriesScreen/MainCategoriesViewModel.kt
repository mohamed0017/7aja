package com.haja.haja.View.ui.MainCategoriesScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.AdsModel
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.model.CategoriesModel

class MainCategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var categories: SingleLiveEvent2<CategoriesModel>
    private lateinit var ads: SingleLiveEvent2<AdsModel>
    private val repository = AppRepository.getInstance
    private var parentID = 0

    fun ssetParentId(id : Int){
        parentID = id
    }
    fun getCategories(parentId : Int?): SingleLiveEvent2<CategoriesModel> {
        return  repository.getCategories(parentId!!, "ar")
    }

    fun getAds(parentId : Int?): SingleLiveEvent2<AdsModel> {
        return  repository.getAds(parentId!!, "ar")
    }

    fun getStartupAd(): SingleLiveEvent2<AdsModel> {
        return  repository.getStartupAd( "ar")
    }
}
