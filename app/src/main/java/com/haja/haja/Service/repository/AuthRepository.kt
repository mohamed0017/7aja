package com.haja.haja.Service.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.haja.haja.Service.ApiService
import com.haja.haja.Service.ServiceGenerator
import com.haja.haja.Service.model.UserModel
import com.haja.haja.Service.enqueue

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
                        Log.i("register/body", errors)
                    }
                    else -> {
                        Log.i("register/body", response.errorBody()?.string())
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
}