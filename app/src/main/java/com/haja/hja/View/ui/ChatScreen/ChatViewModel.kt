package com.haja.hja.View.ui.ChatScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.hja.Service.model.ChatsModel
import com.haja.hja.Service.model.DefultResponse
import com.haja.hja.Service.model.UserChatModel
import com.haja.hja.Service.repository.ChatRepository
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.SingleLiveEvent2
import com.haja.hja.Utils.TOKEN

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = ChatRepository(token.toString())
    private var chats: MutableLiveData<ChatsModel>? = null

    fun addMessage(message: HashMap<String, String>): SingleLiveEvent2<DefultResponse> {
        return repository.addChatMessage(message)
    }


    fun getAllChats(): MutableLiveData<ChatsModel>? {
        return if (chats == null) {
            chats = repository.getAllChats()
            chats
        } else
            chats
    }

    fun getUserChat(userId: Int): SingleLiveEvent2<UserChatModel> {
        return repository.getUserChat(userId)
    }

    fun deleteMessage(messageId: Int): SingleLiveEvent2<DefultResponse> {
        return repository.deleteMessageChat(messageId)
    }
}