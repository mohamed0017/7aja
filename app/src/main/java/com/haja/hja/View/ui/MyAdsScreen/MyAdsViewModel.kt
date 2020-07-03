package com.haja.hja.View.ui.MyAdsScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.hja.Service.model.DefultResponse
import com.haja.hja.Service.model.ProductsModel
import com.haja.hja.Service.repository.ProductRepository
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.SingleLiveEvent2
import com.haja.hja.Utils.TOKEN
import com.haja.hja.Utils.USERID

class MyAdsViewModel (application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = ProductRepository(token.toString())

    fun getMyProducts() : SingleLiveEvent2<ProductsModel> {
         val userId = SharedPreferenceUtil(getApplication()).getString(USERID, "0")?.toInt()
        return  repository.getMyProducts(userId!!)
    }

    fun removeProduct(productId : Int) : SingleLiveEvent2<DefultResponse> {
        return  repository.removeProduct(productId)
    }
}
