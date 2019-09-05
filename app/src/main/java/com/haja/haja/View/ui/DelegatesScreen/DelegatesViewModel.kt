package com.haja.haja.View.ui.DelegatesScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel;
import com.haja.haja.Service.model.ContactUsModel
import com.haja.haja.Service.model.DefultResponse
import com.haja.haja.Service.model.DelegatesModel
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.Utils.TOKEN

class DelegatesViewModel (application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = AppRepository(token.toString())

    fun getDelegates(): SingleLiveEvent2<DelegatesModel> {
        return repository.getDelegates()
    }
}
