package com.haja.hja.View.ui.ChatsScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.hja.Service.model.ChatsModel
import com.haja.hja.Service.repository.ChatRepository
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.TOKEN

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
