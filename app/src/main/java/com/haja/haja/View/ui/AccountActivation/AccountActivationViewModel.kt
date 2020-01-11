package com.haja.haja.View.ui.AccountActivation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.haja.Service.model.DefultResponse
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Service.repository.AuthRepository
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.TOKEN
import okhttp3.ResponseBody

class AccountActivationViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN,"")
    private val authRepository = AuthRepository(token.toString())
    private val appRepository = AppRepository(token.toString())

    fun accountActivation(code : String, token :String): MutableLiveData<DefultResponse> {
        return authRepository.accountActivation(code, token)
    }


    fun sendSms(mobile : String , message : String): MutableLiveData<ResponseBody> {
        return appRepository.sendSms(mobile, message)
    }
   /* fun resendActivationCode( token :String): MutableLiveData<AuthResponseModel> {
        return authRepository.resendActivationCode(token)
    }*/
}
