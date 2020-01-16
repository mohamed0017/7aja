package com.haja.haja.View.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.haja.Service.model.DefultResponse
import com.haja.haja.Service.model.UserModel
import com.haja.haja.Service.repository.AuthRepository
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.TOKEN
import com.haja.haja.Utils.USERID
import retrofit2.http.QueryMap

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(application).getString(TOKEN, "")
    private val userId = SharedPreferenceUtil(application).getString(USERID, "0")
    private val authRepository = AuthRepository(token.toString())

    fun getProfile(id : Int): MutableLiveData<UserModel> {
       return authRepository.getProfile(id)
    }

    fun editProfile(@QueryMap messageMap: HashMap<String, String>): MutableLiveData<DefultResponse> {
        return authRepository.editProfile(userId!!.toInt(),messageMap)
    }
}
