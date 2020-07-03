package com.haja.hja.View.ui.UserAdsScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.hja.Service.model.ProductsModel
import com.haja.hja.Service.repository.ProductRepository
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.SingleLiveEvent2
import com.haja.hja.Utils.TOKEN

class UserAdsViewModel (application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = ProductRepository(token.toString())

    fun getMyProducts(userId : Int) : SingleLiveEvent2<ProductsModel> {
        return  repository.getMyProducts(userId)
    }
}
