package com.haja.haja.View.ui.Register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.haja.Service.model.UserModel
import com.haja.haja.Service.repository.AuthRepository
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.TOKEN

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val authRepository = AuthRepository(token.toString())

    fun register(map: HashMap<String, String>): MutableLiveData<UserModel> {
        return authRepository.register(map)
    }

}