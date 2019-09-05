package com.haja.haja.View.ui.MyAdsScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.DefultResponse
import com.haja.haja.Service.model.MyProductsmodel
import com.haja.haja.Service.model.ProductsModel
import com.haja.haja.Service.repository.ProductRepository
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.Utils.TOKEN
import com.haja.haja.Utils.USERID

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
