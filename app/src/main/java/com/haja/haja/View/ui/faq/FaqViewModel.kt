package com.haja.haja.View.ui.faq

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.haja.Service.model.FaqModel
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.TOKEN

class FaqViewModel(application: Application) : AndroidViewModel(application) {

    val token = SharedPreferenceUtil(getApplication()).getString(TOKEN,"")
    val repository = AppRepository(token!!)

    fun getFaq(): MutableLiveData<FaqModel> {
        return repository.getFaq()
    }

}
