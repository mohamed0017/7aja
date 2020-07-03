package com.haja.hja.View.ui.Register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.hja.Service.model.UserModel
import com.haja.hja.Service.repository.AppRepository
import com.haja.hja.Service.repository.AuthRepository
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.TOKEN
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