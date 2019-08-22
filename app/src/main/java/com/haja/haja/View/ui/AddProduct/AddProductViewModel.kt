package com.haja.haja.View.ui.AddProduct

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import com.haja.haja.Service.model.AddProAttributesModel
import com.haja.haja.Service.model.AddProductResponse
import com.haja.haja.Service.model.AdsModel
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Service.repository.ProductRepository
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.model.CategoriesModel
import okhttp3.MultipartBody

class AddProductViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var categories: SingleLiveEvent2<CategoriesModel>
    private lateinit var ads: SingleLiveEvent2<AdsModel>
    private val repository = AppRepository.getInstance
    private val productRepository = ProductRepository.getInstance
    private var parentID = 0
    private var productData = HashMap<String, String>()
    private var productAttributes = HashMap<String, List<String>>()
    private lateinit var parts: List<MultipartBody.Part>

    fun setParentId(id: Int) {
        parentID = id
    }

    fun getCategories(): SingleLiveEvent2<CategoriesModel> {
        categories = repository.getCategories(parentID, "ar")
        return categories
    }

    fun setProductAttributes(attributes : HashMap<String, List<String>>){
        productAttributes = attributes
    }
    fun getCategoryAttributes(catId: Int?): SingleLiveEvent2<AddProAttributesModel> {
        return productRepository.getCategoryAttributes(catId!!, "ar")
    }

    fun addProduct(): SingleLiveEvent2<AddProductResponse> {
        return productRepository.addProduct(productData, parts , productAttributes)
    }

    fun setProductData(map : HashMap<String, String>){
        productData = map
    }

    fun setImages(images: List<MultipartBody.Part>) {
        parts = images
    }

    fun getCategoriesName() = Transformations.map(getCategories()) { categories ->
        if (categories != null) {
            val names = categories.data?.size?.let { ArrayList<String>(it) }
            for (i in categories.data?.indices!!) {
                names?.add(categories.data[i]?.name.toString())
            }
            names
        } else
            null
    }

    fun isCategoryHaveSub(postition: Int): Boolean {
        return categories.value?.data?.get(postition)?.countSubCat != 0
    }

    fun getCatId(postition: Int): Int? {
        return categories.value?.data?.get(postition)?.id
    }

    fun getAds(parentId: Int?): SingleLiveEvent2<AdsModel> {
        return repository.getAds(parentId!!, "ar")
    }

    fun getStartupAd(): SingleLiveEvent2<AdsModel> {
        return repository.getStartupAd("ar")
    }
}
