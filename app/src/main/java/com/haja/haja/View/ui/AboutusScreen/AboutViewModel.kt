package com.haja.haja.View.ui.AboutusScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.StaticPagesModel
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Utils.SingleLiveEvent2

class AboutViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository.getInstance

    fun getPageContent(type: Int): SingleLiveEvent2<StaticPagesModel> {
        return repository.getStaticPages(type)
    }
}
