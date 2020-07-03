package com.haja.hja.View.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.hja.Service.model.DefultResponse
import com.haja.hja.Service.model.UserModel
import com.haja.hja.Service.repository.AuthRepository
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.TOKEN
import com.haja.hja.Utils.USERID
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
