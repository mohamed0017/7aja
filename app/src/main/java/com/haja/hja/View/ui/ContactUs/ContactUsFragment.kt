package com.haja.hja.View.ui.ContactUs

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView

import com.haja.hja.R
import com.haja.hja.Service.model.ContactDetailsModel
import com.haja.hja.Service.model.ContactUsModel
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.contact_phones_dialog.*
import kotlinx.android.synthetic.main.contact_us_fragment.*

class ContactUsFragment : Fragment() {

    companion object {
        fun newInstance(contactDetails: ContactDetailsModel?) = ContactUsFragment().apply {
            arguments = Bundle().apply {
                putParcelable("contactDetails" ,contactDetails)
            }
        }
    }

    private lateinit var viewModel: ContactUsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contact_us_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.categoriesBarBack?.visibility = View.VISIBLE
        activity?.categoriesBarMenu?.visibility = View.GONE

        activity?.appBarTitle?.text = resources.getString(R.string.support)

        viewModel = ViewModelProviders.of(this).get(ContactUsViewModel::class.java)
        val contactDetails = arguments?.getParcelable("contactDetails") as ContactDetailsModel

        callUs.setOnClickListener {
          val dialog =  MaterialDialog(context!!, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                customView(R.layout.contact_phones_dialog)
            }
            dialog.mobile1.text = contactDetails.tel
            dialog.mobile1.setOnClickListener {
                if (contactDetails.tel != null) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contactDetails.tel, null))
                    startActivity(intent)
                }
            }
            dialog.mobile2.text = contactDetails.mobile
            dialog.mobile2.setOnClickListener {
                if (contactDetails.tel != null) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contactDetails.mobile, null))
                    startActivity(intent)
                }
            }
            dialog.mobile3.text = contactDetails.mobile2
            dialog.mobile3.setOnClickListener {
                if (contactDetails.tel != null) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contactDetails.mobile2, null))
                    startActivity(intent)
                }
            }

        }
        val progress = CustomProgressBar.showProgressBar(context!!)
        sendContactBut.setOnClickListener {
            if (isValidContactData()) {
                progress.show()
                viewModel.sendContactUS(getContactInfo()).observe(this, Observer { result ->
                    progress.dismiss()
                    if (result != null) {
                        if (result.result == true) {
                            makeToast(context!!, resources.getString(R.string.send_message_done))
                            activity?.onBackPressed()
                        } else
                            makeToast(context!!, result.errorMesage.toString())
                    } else
                        makeToast(context!!, resources.getString(R.string.error))
                })
            }
        }

    }

    private fun getContactInfo(): ContactUsModel {
        val contactUs = ContactUsModel()
        contactUs.name = contactName.text.toString()
        contactUs.mobile = contactPhone.text.toString()
        contactUs.subject = `contactTitleُ`.text.toString()
        contactUs.message = contactMessage.text.toString()
        return contactUs
    }

    private fun isValidContactData(): Boolean {
        val name = contactName.text.toString()
        val phone = contactPhone.text.toString()
        val title = `contactTitleُ`.text.toString()
        val description = contactMessage.text.toString()
        return when {
            name.isEmpty() -> {
                contactName.error = resources.getString(R.string.requird)
                false
            }
            phone.isEmpty() -> {
                contactPhone.error = resources.getString(R.string.requird)
                false
            }
            title.isEmpty() -> {
                `contactTitleُ`.error = resources.getString(R.string.requird)
                false
            }
            description.isEmpty() -> {
                contactMessage.error = resources.getString(R.string.requird)
                false
            }
            else -> true
        }
    }
}
