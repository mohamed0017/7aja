package com.haja.hja.View.ui.Register

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE
import com.example.easywaylocation.Listener
import com.haja.hja.R
import com.haja.hja.Service.model.UserDataModel
import com.haja.hja.Utils.LANG
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.ValidationUtils.*
import com.haja.hja.Utils.inTransaction
import com.haja.hja.View.ui.AccountActivation.AccountActivationFragment
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main_categories.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.register_fragment.*
import java.util.*
import kotlin.collections.HashMap


class RegisterFragment : Fragment(), Listener {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel
    var easyWayLocation: EasyWayLocation? = null
    private var lati: Double? = null
    private var longi: Double? = null
    private var generatedCode = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        // activity?.bottomNavigation?.visibility = View.GONE
        activity?.appBarTitle?.text = resources.getString(R.string.registerNew)
        activity?.categoriesBarBack?.visibility = View.VISIBLE
        activity?.categoriesBarMenu?.visibility = View.GONE
        activity?.catBarSearch?.visibility = View.GONE

        registerShowPass.setOnClickListener {
            registerPass.inputType = EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        registerPhoneCode.keyListener = null
        configLocationPremation()
        registerBut.setOnClickListener {
            if (isValidUserInputs()) {
                registerProgress.visibility = View.VISIBLE
                registerBut.visibility = View.GONE
                viewModel.register(userRegistrationInfo()).observe(this, Observer { user ->
                    registerProgress.visibility = View.GONE
                    registerBut.visibility = View.VISIBLE
                    if (user != null) {
                        if (user.result == true) {
                            user.data?.activitationCode = generatedCode
                            sendSmsWithCodeToUser(user.data?.mobile.toString())
                            goToActivationAccount(user.data)
                            activity?.nav_view?.menu?.findItem(R.id.nav_logOut)?.isVisible = true
                        } else {
                            val lang = SharedPreferenceUtil(context!!).getString(LANG, "")
                            if (lang == "ar")
                                makeToast(context!!, user.errorMesage.toString())
                            else
                                makeToast(context!!, user.errorMesageEn.toString())

                            /*     when {
                                     user.errorMesage?.mobile != null -> makeToast(
                                         context!!,
                                         user.errorMesage.mobile[0].toString()
                                     )
                                     user.errorMesage?.email != null -> makeToast(
                                         context!!,
                                         user.errorMesage.email[0].toString()
                                     )
                                     user.errorMesage?.name != null -> makeToast(
                                         context!!,
                                         user.errorMesage.name[0].toString()
                                     )
                                 }*/
                        }
                    } else
                        makeToast(context!!, resources.getString(R.string.error))
                })
            }

        }
    }

    override fun locationCancelled() {
    }

    override fun locationOn() {
        easyWayLocation?.beginUpdates()
        lati = easyWayLocation?.latitude
        longi = easyWayLocation?.longitude
    }

    override fun onPositionChanged() {
        easyWayLocation?.beginUpdates()
        lati = easyWayLocation?.latitude
        longi = easyWayLocation?.longitude
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOCATION_SETTING_REQUEST_CODE -> easyWayLocation?.onActivityResult(resultCode)
        }
    }

    private fun goToActivationAccount(user: UserDataModel?) {
        fragmentManager?.inTransaction {
            replace(R.id.mainContainer, AccountActivationFragment.newInstance(user))
        }
    }

    private fun sendSmsWithCodeToUser(mobile: String) {
        var sss = " تم التسجيل في تطبيق حاجة كود التفعيل هو : $generatedCode"
        //sss = sss.replace(" ", "+")
        viewModel.sendSms(mobile, sss)
            .observe(this, Observer {

            })
    }

    private fun isValidUserInputs(): Boolean {
        val name = registerName.text.toString()
        val phone = registerPhone.text.toString()
        val pass = registerPass.text.toString()
        val email = registerEmail.text.toString()
        val confirmPass = `registerConfirmPassُ`.text.toString()

        return if (!isValidUserName(name)) {
            registerName.error = resources.getString(R.string.correct_name)
            false
        } else if (!isValidPhone(phone)) {
            registerPhone.error = resources.getString(R.string.correct_phone)
            false
        } else if (!isValidPassword(pass)) {
            registerPass.error = resources.getString(R.string.correct_pass)
            false

        }/* else if (!isValidEmail(email)) {
            registerEmail.error = resources.getString(R.string.correct_email)
            false
        }*/ else if (pass != confirmPass) {
            `registerConfirmPassُ`.error = resources.getString(R.string.mismatch_pass)
            false
        } else
            true
    }

    private fun userRegistrationInfo(): HashMap<String, String> {
        val map = HashMap<String, String>()
        map["name"] = registerName.text.toString()
      //  map["mobile"] = "${registerPhone.text}" // TODO just for debug
        map["mobile"] = "965${registerPhone.text}"
        map["email"] = registerEmail.text.toString()
        map["password"] = registerPass.text.toString()
        map["c_password"] = `registerConfirmPassُ`.text.toString()
        map["activitation_code"] = generateCode()
        map["onesignal_id"] = "test"  //TODO replace onesignal_id when setup onsignal project
        map["latitude"] = lati.toString()
        map["longitude"] = longi.toString()
        return map
    }

    private fun generateCode(): String {
        val random = Random()
        generatedCode = String.format(Locale.ENGLISH, "%04d", random.nextInt(10000))
        Log.i("generateCode", generatedCode)
        return generatedCode
    }

    private fun configLocationPremation() {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionRationaleShouldBeShown(
                    permission: com.karumi.dexter.listener.PermissionRequest?,
                    token: PermissionToken?
                ) {}

                @SuppressLint("MissingPermission")
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    easyWayLocation = EasyWayLocation(context!!)
                    easyWayLocation?.setListener(this@RegisterFragment)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                }

            }).check()
    }

    override fun onResume() {
        super.onResume()
        // make the device update its location
        easyWayLocation?.beginUpdates()
    }

    override fun onPause() {
        // stop location updates (saves battery)
        easyWayLocation?.endUpdates()
        super.onPause()
    }
}
