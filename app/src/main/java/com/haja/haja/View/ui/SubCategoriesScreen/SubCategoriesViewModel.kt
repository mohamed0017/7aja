package com.haja.haja.View.ui.SubCategoriesScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.model.CategoriesModel

class SubCategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var categories: SingleLiveEvent2<CategoriesModel>
    private val repository = AppRepository.getInstance
    private var parentID = 0

    fun ssetParentId(id: Int) {
        parentID = id
    }

    fun getCategories(): SingleLiveEvent2<CategoriesModel> {
        categories = repository.getCategories(parentID, "ar")
        return categories
    }
}
