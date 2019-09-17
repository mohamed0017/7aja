package com.haja.haja.View.ui.ChatsScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.haja.Service.model.ChatsModel
import com.haja.haja.Service.repository.ChatRepository
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.TOKEN

class ChatsViewModel (application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = ChatRepository(token.toString())
    private var chats: MutableLiveData<ChatsModel>? = null

    fun getAllChats(): MutableLiveData<ChatsModel>? {
        return if (chats == null) {
            chats = repository.getAllChats()
            chats
        } else
            chats
    }
}
