package com.haja.hja.View.ui.NotificationsHistory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.hja.Service.model.NotificationsResponse
import com.haja.hja.Service.repository.AppRepository
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.SingleLiveEvent2
import com.haja.hja.Utils.TOKEN
import com.haja.hja.Utils.USERID

class NotificationsListViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = AppRepository(token.toString())

    fun getNotifications(): SingleLiveEvent2<NotificationsResponse> {
        val userId = SharedPreferenceUtil(getApplication()).getString(USERID, "0")?.toInt()
        return repository.getNotifications(userId!!)
    }
}
