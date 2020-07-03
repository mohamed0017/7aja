package com.haja.hja.Service.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.haja.hja.Service.ApiService
import com.haja.hja.Service.ServiceGenerator
import com.haja.hja.Service.model.UserModel
import com.haja.hja.Service.enqueue
import com.haja.hja.Service.model.DefultResponse

class AuthRepository(token:String){

    private var apiService: ApiService? = null

    init {
        apiService = ServiceGenerator(token).createService
    }

    fun login(map: HashMap<String, String>): MutableLiveData<UserModel> {
        val result = MutableLiveData<UserModel>()
        val call = apiService?.login(map)
        call?.enqueue {
            onResponse = { response ->
                Log.i("login", response.code().toString())
                when (response.code() / 100) {
                    2 -> {
                        result.value = response.body()
                        Log.i("login/body", response.body().toString())
                    }
                    4 -> {
                        val errors = response.errorBody()?.string()
                        val gson = Gson()
                        val errorResponse =
                            gson.fromJson(errors, UserModel::class.java)
                        result.value = errorResponse
                        Log.i("login/body", errors)
                    }
                    else -> {
                        Log.i("login/body", response.errorBody()?.string())
                        result.value = null
                    }
                }
            }
            onFailure = { t ->
                Log.e("login", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun register(map: HashMap<String, String>): MutableLiveData<UserModel> {
        val result = MutableLiveData<UserModel>()
        val call = apiService?.register(map)
        call?.enqueue {
            onResponse = { response ->
                Log.i("register", response.code().toString())
                when (response.code() / 100) {
                    2 -> {
                        result.value = response.body()
                        Log.i("register/body", response.body().toString())
                    }
                    4 -> {
                        val errors = response.errorBody()?.string()
                        val gson = Gson()
                        val errorResponse =
                            gson.fromJson(errors, UserModel::class.java)
                        result.value = errorResponse
                        Log.i("register/errors", errors)
                    }
                    else -> {
                        Log.i("register/errors", response.errorBody()?.string())
                        result.value = null
                    }
                }
            }
            onFailure = { t ->
                Log.e("register", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun accountActivation(code : String, token: String): MutableLiveData<DefultResponse> {
        val result = MutableLiveData<DefultResponse>()
        val call = apiService?.accountActivation(code, "Bearer $token")
        call?.enqueue {
            Log.e("accountActivation/url", call.request().url.toString())
            Log.e("accountActivation/header", call.request().header("Authorization").toString())
            onResponse = { response ->
                Log.i("accountActivation", response.code().toString())
                when (response.code() / 100) {
                    2 -> {
                        result.value = response.body()
                        Log.i("accountActivation/body", response.body().toString())
                    }
                    else -> {
                        Log.i("accountActivation/error", response.errorBody()?.string())
                        result.value = null
                    }
                }
            }
            onFailure = { t ->
                Log.e("accountActive/onFailure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun forgetPass(mobile : String): MutableLiveData<DefultResponse> {
        val result = MutableLiveData<DefultResponse>()
        val call = apiService?.forgetPass(mobile)
        call?.enqueue {
            Log.e("forgetPass/url", call.request().url.toString())
            onResponse = { response ->
                Log.i("forgetPass", response.code().toString())
                when (response.code() / 100) {
                    2 -> {
                        result.value = response.body()
                        Log.i("forgetPass/body", response.body().toString())
                    }
                    else -> {
                        Log.i("forgetPass/error", response.errorBody()?.string())
                        result.value = null
                    }
                }
            }
            onFailure = { t ->
                Log.e("forgetPass/onFailure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun getProfile(id : Int): MutableLiveData<UserModel> {
        val result = MutableLiveData<UserModel>()
        val call = apiService?.getProfile(id)
        call?.enqueue {
            Log.e("getProfile/url", call.request().url.toString())
            onResponse = { response ->
                Log.i("getProfile", response.code().toString())
                when (response.code() / 100) {
                    2 -> {
                        result.value = response.body()
                        Log.i("getProfile/body", response.body().toString())
                    }
                    else -> {
                        Log.i("getProfile/error", response.errorBody()?.string())
                        result.value = null
                    }
                }
            }
            onFailure = { t ->
                Log.e("getProfile/onFailure", t!!.message)
                result.value = null
            }
        }
        return result
    }

    fun editProfile(id : Int, messageMap: HashMap<String, String>): MutableLiveData<DefultResponse> {
        val result = MutableLiveData<DefultResponse>()
        val call = apiService?.editProfile(id,messageMap)
        call?.enqueue {
            Log.e("editProfile/url", call.request().url.toString())
            onResponse = { response ->
                Log.i("editProfile", response.code().toString())
                when (response.code() / 100) {
                    2 -> {
                        result.value = response.body()
                        Log.i("editProfile/body", response.body().toString())
                    }
                    else -> {
                        Log.i("editProfile/error", response.errorBody()?.string())
                        result.value = null
                    }
                }
            }
            onFailure = { t ->
                Log.e("editProfile/onFailure", t!!.message)
                result.value = null
            }
        }
        return result
    }
}