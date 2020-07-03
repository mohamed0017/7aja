package com.haja.hja.Service.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.haja.hja.Service.ApiService
import com.haja.hja.Service.ServiceGenerator
import com.haja.hja.Service.enqueue
import com.haja.hja.Service.model.ChatsModel
import com.haja.hja.Service.model.DefultResponse
import com.haja.hja.Service.model.UserChatModel
import com.haja.hja.Utils.SingleLiveEvent2

class ChatRepository(token: String) {

    private var apiService: ApiService? = null

    init {
        apiService = ServiceGenerator(token).createService
    }

    fun addChatMessage(message : HashMap<String, String>): SingleLiveEvent2<DefultResponse> {
        val result = SingleLiveEvent2<DefultResponse>()
        val call = apiService?.addChatMessage(message)
        call?.enqueue {
            onResponse = { response ->
                Log.i("addChatMessage", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("addChatMessage/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result

    }

    fun getAllChats(): MutableLiveData<ChatsModel> {
        val result = MutableLiveData<ChatsModel>()
        val call = apiService?.getAllChats()
        call?.enqueue {
            onResponse = { response ->
                Log.i("getAllChats", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getAllChats/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result

    }

    fun getUserChat(userId : Int): SingleLiveEvent2<UserChatModel> {
        val result = SingleLiveEvent2<UserChatModel>()
        val call = apiService?.getUserChat(userId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("getUserChat", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("getUserChat/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result

    }

    fun deleteMessageChat(messageId : Int ): SingleLiveEvent2<DefultResponse> {
        val result = SingleLiveEvent2<DefultResponse>()
        val call = apiService?.deleteChat(messageId)
        call?.enqueue {
            onResponse = { response ->
                Log.i("deleteChat", response.code().toString())
                if (response.code() / 100 == 2)
                    result.value = response.body()
                else
                    result.value = null
            }
            onFailure = { t ->
                Log.i("deleteChat/Failure", t!!.message + "..")
                result.value = null
            }
        }
        return result

    }
}