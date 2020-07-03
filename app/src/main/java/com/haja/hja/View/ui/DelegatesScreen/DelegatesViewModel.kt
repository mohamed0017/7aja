package com.haja.hja.View.ui.DelegatesScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.hja.Service.model.DelegatesModel
import com.haja.hja.Service.repository.AppRepository
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.SingleLiveEvent2
import com.haja.hja.Utils.TOKEN

class DelegatesViewModel (application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = AppRepository(token.toString())

    fun getDelegates(): SingleLiveEvent2<DelegatesModel> {
        return repository.getDelegates()
    }
}
