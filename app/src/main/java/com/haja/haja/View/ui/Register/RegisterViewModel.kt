package com.haja.haja.View.ui.Register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.haja.Service.model.UserModel
import com.haja.haja.Service.repository.AuthRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository.getInstance

    fun register(map: HashMap<String, String>): MutableLiveData<UserModel> {
        return authRepository.register(map)
    }

}