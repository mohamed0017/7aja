package com.haja.hja.View.ui.AddProduct

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.haja.hja.Service.model.AdPriceModel
import com.haja.hja.Service.model.AddProAttributesModel
import com.haja.hja.Service.model.AddProductResponse
import com.haja.hja.Service.model.AdsModel
import com.haja.hja.Service.repository.AppRepository
import com.haja.hja.Service.repository.ProductRepository
import com.haja.hja.Utils.*
import com.haja.hja.model.CategoriesModel
import okhttp3.MultipartBody

class AddProductViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = AppRepository(token.toString())
    private lateinit var categories: MutableLiveData<CategoriesModel>
    private val productRepository = ProductRepository(token.toString())

    private var parentID = 0
    private var productData = HashMap<String, String>()
    private var productAttributes = HashMap<String, String>()
    private lateinit var parts: List<MultipartBody.Part>
    val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")

    fun setParentId(id: Int) {
        parentID = id
    }

    fun getCategories(): MutableLiveData<CategoriesModel> {

        categories = repository.getCategories(parentID, "$lang")
        return categories
    }

    fun getAdPrice(): SingleLiveEvent2<AdPriceModel> {
        val userId = SharedPreferenceUtil(getApplication()).getString(USERID , "0")
        return productRepository.getAdPrice(userId.toString())
    }

    fun setProductAttributes(attributes : HashMap<String, String>){
        productAttributes = attributes
    }
    fun getCategoryAttributes(catId: Int?): SingleLiveEvent2<AddProAttributesModel> {
        return productRepository.getCategoryAttributes(catId!!, "$lang")
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
        return repository.getAds(parentId!!, "$lang")
    }

    fun getStartupAd(): SingleLiveEvent2<AdsModel> {
         val userId = SharedPreferenceUtil(getApplication()).getString(USERID, "0")
        return repository.getStartupAd("$lang",userId!!.toInt())
    }
}
