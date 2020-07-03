package com.haja.hja.View.ui.faq

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haja.hja.Service.model.FaqModel
import com.haja.hja.Service.repository.AppRepository
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.TOKEN

class FaqViewModel(application: Application) : AndroidViewModel(application) {

    val token = SharedPreferenceUtil(getApplication()).getString(TOKEN,"")
    val repository = AppRepository(token!!)

    fun getFaq(): MutableLiveData<FaqModel> {
        return repository.getFaq()
    }

}
