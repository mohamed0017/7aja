package com.haja.haja.View.ui.MoreScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.haja.Service.model.ContactDetailsModel
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Service.repository.AuthRepository
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.Utils.TOKEN

class MoreViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = AppRepository(token.toString())
    private val authRepository = AuthRepository(token.toString())
    private var contactDetailsModel: MutableLiveData<ContactDetailsModel>? = null

    fun getContactDetails(): MutableLiveData<ContactDetailsModel>? {
        return if (contactDetailsModel == null) {
            contactDetailsModel = repository.getContactDetails()
            contactDetailsModel
        } else
            contactDetailsModel
    }

    fun getUserInfo(){

    }
}
