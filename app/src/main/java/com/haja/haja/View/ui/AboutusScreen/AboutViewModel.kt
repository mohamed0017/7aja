package com.haja.haja.View.ui.AboutusScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.StaticPagesModel
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.Utils.TOKEN

class AboutViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = AppRepository(token.toString())

    fun getPageContent(type: Int): SingleLiveEvent2<StaticPagesModel> {
        return repository.getStaticPages(type)
    }
}
