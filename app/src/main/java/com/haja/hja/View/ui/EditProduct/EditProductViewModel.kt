package com.haja.hja.View.ui.EditProduct

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.hja.Service.model.*
import com.haja.hja.Service.repository.AppRepository
import com.haja.hja.Service.repository.ProductRepository
import com.haja.hja.Utils.*
import com.haja.hja.model.CategoriesModel
import okhttp3.MultipartBody

class EditProductViewModel(application: Application) : AndroidViewModel(application){

    private var productData = HashMap<String, String>()
    private var productAttributes = HashMap<String, String>()
    private var parts: List<MultipartBody.Part>? = null
    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val lang = SharedPreferenceUtil(getApplication()).getString(LANG, "ar")
    private val productRepository = ProductRepository(token.toString())
    private var parentID = 0
    private lateinit var categories: MutableLiveData<CategoriesModel>
    private val repository = AppRepository(token.toString())

    fun getProduct(productId : Int , userId : Int): SingleLiveEvent2<ProductsModel> {
        return productRepository.getSingleProduct(productId, lang.toString(), userId)
    }

    fun editProduct(productId : Int): SingleLiveEvent2<AddProductResponse> {
        return productRepository.editProduct(productId , productData, parts , productAttributes)
    }

    fun deleteImage(productId : Int , imageId : Int): SingleLiveEvent2<DefultResponse> {
        return productRepository.deleteProductImg(productId, imageId)
    }

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


    fun setProductData(map : HashMap<String, String>){
        productData = map
    }

    fun setImages(images: List<MultipartBody.Part>) {
        parts = images
    }
}