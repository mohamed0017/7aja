package com.haja.haja.View.ui.LoginScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.haja.Service.model.UserModel
import com.haja.haja.Service.repository.AuthRepository
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.TOKEN

class LoginViewModel (application: Application) : AndroidViewModel(application){


    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val authRepository = AuthRepository(token.toString())

    fun login(map: HashMap<String,String>): MutableLiveData<UserModel> {
        return authRepository.login(map)
    }

}
