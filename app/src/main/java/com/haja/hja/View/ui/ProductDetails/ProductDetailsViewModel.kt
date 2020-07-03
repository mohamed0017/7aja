package com.haja.hja.View.ui.ProductDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.hja.Service.model.DefultResponse
import com.haja.hja.Service.model.FavoriteModel
import com.haja.hja.Service.model.ProductReportResponse
import com.haja.hja.Service.model.ProductsModel
import com.haja.hja.Service.repository.CategoriesRepository
import com.haja.hja.Service.repository.ProductRepository
import com.haja.hja.Utils.*

class ProductDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = CategoriesRepository(token.toString())
    private val productRepository = ProductRepository(token.toString())

    fun addToFav(productId: Int): SingleLiveEvent2<FavoriteModel> {
        return repository.addToFavorite(productId)
    }

    fun removeFromFav(productId: Int): SingleLiveEvent2<FavoriteModel> {
        return repository.removeProductFromFavorite(productId)
    }

    fun sendProductReport(map: HashMap<String, String>): SingleLiveEvent2<ProductReportResponse> {
        return productRepository.productReport(map)
    }

    fun productView(productId: Int): SingleLiveEvent2<DefultResponse> {
        return productRepository.productView(productId)
    }

    fun getSingleProduct(productId: Int): SingleLiveEvent2<ProductsModel> {
        val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")
        val userId = SharedPreferenceUtil(getApplication()).getString(USERID, "0")?.toInt()
        return productRepository.getSingleProduct(productId, lang.toString(), userId!!)
    }
}
