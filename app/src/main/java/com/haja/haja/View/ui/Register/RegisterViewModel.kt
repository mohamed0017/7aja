package com.haja.haja.View.ui.Register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.haja.Service.model.UserModel
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Service.repository.AuthRepository
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.TOKEN
import okhttp3.ResponseBody

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val authRepository = AuthRepository(token.toString())
    private val appRepository = AppRepository(token.toString())

    fun register(map: HashMap<String, String>): MutableLiveData<UserModel> {
        return authRepository.register(map)
    }

    fun sendSms(mobile : String , message : String): MutableLiveData<ResponseBody> {
        return appRepository.sendSms(mobile, message)
    }
}