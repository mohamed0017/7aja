package com.haja.hja.View.ui.AboutusScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.hja.Service.model.StaticPagesModel
import com.haja.hja.Service.repository.AppRepository
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.SingleLiveEvent2
import com.haja.hja.Utils.TOKEN

class AboutViewModel(application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = AppRepository(token.toString())

    fun getPageContent(type: Int): SingleLiveEvent2<StaticPagesModel> {
        return repository.getStaticPages(type)
    }
}
