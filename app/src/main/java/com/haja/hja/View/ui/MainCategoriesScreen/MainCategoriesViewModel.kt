package com.haja.hja.View.ui.MainCategoriesScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.hja.Service.model.AdsModel
import com.haja.hja.Service.model.SliderImgesModel
import com.haja.hja.Service.repository.AppRepository
import com.haja.hja.Utils.*
import com.haja.hja.model.CategoriesData
import com.haja.hja.model.CategoriesModel

class MainCategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")
    private val repository = AppRepository(token.toString())
    private var parentID = 0
    private var mainCategories : ArrayList<CategoriesData>? = null

    fun setMainCategories(categories: ArrayList<CategoriesData>) {
        mainCategories =  ArrayList<CategoriesData>()
         mainCategories?.addAll(categories)
    }

    fun getMainCategories(): ArrayList<CategoriesData>? {
        return mainCategories
    }
    fun ssetParentId(id : Int){
        parentID = id
    }
    fun getCategories(parentId : Int?): MutableLiveData<CategoriesModel> {
        return  repository.getCategories(parentId!!, "$lang")
    }


    fun getMainSliderImages(): MutableLiveData<SliderImgesModel> {
        val userId = SharedPreferenceUtil(getApplication()).getString(USERID, "0")
        return  repository.getMainSliderImages(userId!!.toInt())
    }

    fun getAds(parentId : Int?): SingleLiveEvent2<AdsModel> {
        return  repository.getAds(parentId!!, "$lang")
    }

    fun getStartupAd(): SingleLiveEvent2<AdsModel> {
        val userId = SharedPreferenceUtil(getApplication()).getString(USERID, "0")
        return  repository.getStartupAd( "$lang", userId!!.toInt())
    }
}
