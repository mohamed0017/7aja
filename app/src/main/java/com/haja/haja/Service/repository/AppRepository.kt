package com.haja.haja.Service.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.haja.haja.Service.ApiService
import com.haja.haja.Service.ServiceGenerator
import com.haja.haja.Service.enqueue
import com.haja.haja.Service.model.*
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.model.CategoriesModel
import okhttp3.ResponseBody
import java.net.URLEncoder

class AppRepository(token: String) {

    private var apiService: ApiService? = null
    private var apiServiceForSms: ApiService? = null

    init {
        apiService = ServiceGenerator(token).createService
        apiServiceForSms = ServiceGenerator(token).createServiceForSms
    }

    fun sendSms(mobile:String,  message: String): MutableLiveData<ResponseBody> {
        val result = MutableLiveData<ResponseBody>()
        val call = apiServiceForSms?.sendSms("201004$mobile",message)
        call?.enqueue {
            onResponse = { response ->
                Log.i("sendSms/url", call.request().url.toString())
                Log.i("sendSms", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("sendSms/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun getCategories(parentId:Int, language: String): MutableLiveData<CategoriesModel> {
        val result = MutableLiveData<CategoriesModel>()
        val call = apiService?.getCategories(parentId = parentId, lang = language)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getCategories", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getCategories/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun getAds(categoryId:Int, language: String): SingleLiveEvent2<AdsModel> {
        val result = SingleLiveEvent2<AdsModel>()
        val call = apiService?.getAds(categoryId = categoryId, lang = language)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getAds", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getAds/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun getStartupAd( language: String, userId: Int): SingleLiveEvent2<AdsModel> {
        val result = SingleLiveEvent2<AdsModel>()
        val call = apiService?.getStartupAd(lang = language , userId = userId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getStartupAd", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getStartupAd/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun getStaticPages( type: Int): SingleLiveEvent2<StaticPagesModel> {
        val result = SingleLiveEvent2<StaticPagesModel>()
        val call = apiService?.getStaticPages(type)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getStaticPages", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getStaticPages/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun getNotifications( userId: Int): SingleLiveEvent2<NotificationsResponse> {
        val result = SingleLiveEvent2<NotificationsResponse>()
        val call = apiService?.getNotifications(userId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getNotifications", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("Notifications/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun contactUS( contactUsModel: ContactUsModel): SingleLiveEvent2<DefultResponse> {
        val result = SingleLiveEvent2<DefultResponse>()
        val call = apiService?.contactUS(contactUsModel)
        call?.enqueue {
            onResponse = { response ->
                Log.i("contactUS", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("contactUS/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun getDelegates(): SingleLiveEvent2<DelegatesModel> {
        val result = SingleLiveEvent2<DelegatesModel>()
        val call = apiService?.getDelegates()
        call?.enqueue {
            onResponse = { response ->
                Log.i("getDelegates", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getDelegates/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun getContactDetails(): MutableLiveData<ContactDetailsModel> {
        val result = MutableLiveData<ContactDetailsModel>()
        val call = apiService?.getContactDetails()
        call?.enqueue {
            onResponse = { response ->
                Log.i("getContactDetails", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getCotDetails/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun getMainSliderImages(userId: Int): MutableLiveData<SliderImgesModel> {
        val result = MutableLiveData<SliderImgesModel>()
        val call = apiService?.getMainSliderImages(userId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getMainSlider", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getMainSlider/Failure", t!!.message)
                result.value = null
            }
        }
        return result
    }
}