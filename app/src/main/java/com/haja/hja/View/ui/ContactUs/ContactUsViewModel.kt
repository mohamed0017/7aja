package com.haja.hja.View.ui.ContactUs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.hja.Service.model.ContactUsModel
import com.haja.hja.Service.model.DefultResponse
import com.haja.hja.Service.repository.AppRepository
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.SingleLiveEvent2
import com.haja.hja.Utils.TOKEN

class ContactUsViewModel (application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = AppRepository(token.toString())

    fun sendContactUS(contactUsModel: ContactUsModel): SingleLiveEvent2<DefultResponse> {
        return repository.contactUS(contactUsModel)
    }
}
