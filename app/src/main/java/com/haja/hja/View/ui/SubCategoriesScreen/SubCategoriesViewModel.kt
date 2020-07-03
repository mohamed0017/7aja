package com.haja.hja.View.ui.SubCategoriesScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.hja.Service.model.AdsModel
import com.haja.hja.Service.repository.AppRepository
import com.haja.hja.Utils.LANG
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.SingleLiveEvent2
import com.haja.hja.Utils.TOKEN
import com.haja.hja.model.CategoriesModel

class SubCategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")
    private val repository = AppRepository(token.toString())
    private var parentID = 0
    private var categories: MutableLiveData<CategoriesModel>? = null

    fun setParentId(id: Int) {
        parentID = id
    }

    fun getCategories(): MutableLiveData<CategoriesModel>? {
        val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")
        return if (categories == null) {
            categories = repository.getCategories(parentID, "$lang")
            categories
        } else
            return categories

    }

    fun getAds(parentId : Int?): SingleLiveEvent2<AdsModel> {
        return  repository.getCatAds(parentId!!, "$lang")
    }

}
