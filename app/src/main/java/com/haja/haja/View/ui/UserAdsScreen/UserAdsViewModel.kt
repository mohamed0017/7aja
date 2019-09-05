package com.haja.haja.View.ui.UserAdsScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel;
import com.haja.haja.Service.model.ProductsModel
import com.haja.haja.Service.repository.ProductRepository
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.Utils.TOKEN

class UserAdsViewModel (application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = ProductRepository(token.toString())

    fun getMyProducts(userId : Int) : SingleLiveEvent2<ProductsModel> {
        return  repository.getMyProducts(userId)
    }
}
