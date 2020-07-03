package com.haja.hja.View.ui.LoginScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.hja.Service.model.DefultResponse
import com.haja.hja.Service.model.UserModel
import com.haja.hja.Service.repository.AuthRepository
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.TOKEN

class LoginViewModel (application: Application) : AndroidViewModel(application){


    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val authRepository = AuthRepository(token.toString())

    fun login(map: HashMap<String,String>): MutableLiveData<UserModel> {
        return authRepository.login(map)
    }

    fun forgetPass(mobile : String): MutableLiveData<DefultResponse> {
        return authRepository.forgetPass(mobile)
    }
}
