package com.haja.haja.View.ui.ContactUs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.haja.haja.Service.model.ContactUsModel
import com.haja.haja.Service.model.DefultResponse
import com.haja.haja.Service.repository.AppRepository
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.SingleLiveEvent2
import com.haja.haja.Utils.TOKEN

class ContactUsViewModel (application: Application) : AndroidViewModel(application) {

    private val token = SharedPreferenceUtil(getApplication()).getString(TOKEN, "")
    private val repository = AppRepository(token.toString())

    fun sendContactUS(contactUsModel: ContactUsModel): SingleLiveEvent2<DefultResponse> {
        return repository.contactUS(contactUsModel)
    }
}
