package com.haja.hja.View.ui.MoreScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.hja.Service.model.ContactDetailsModel
import com.haja.hja.Service.model.UserModel
import com.haja.hja.Service.repository.AppRepository
import com.haja.hja.Service.repository.AuthRepository
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.TOKEN

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

    fun getProfile(id : Int): MutableLiveData<UserModel> {
        return authRepository.getProfile(id)
    }
}
