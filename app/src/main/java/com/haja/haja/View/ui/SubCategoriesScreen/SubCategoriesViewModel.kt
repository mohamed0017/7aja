package com.haja.haja.View.ui.SubCategoriesScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Utils.LANG
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.Utils.TOKEN
import com.haja.haja.model.CategoriesModel

class SubCategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = AppRepository(token.toString())
    private var parentID = 0
    private var categories: MutableLiveData<CategoriesModel>? = null

    fun ssetParentId(id: Int) {
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
}
