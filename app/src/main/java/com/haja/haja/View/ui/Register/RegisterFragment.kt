package com.haja.haja.View.ui.Register

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.haja.haja.R
import com.haja.haja.Utils.ValidationUtils.*
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import kotlinx.android.synthetic.main.register_fragment.*
import android.widget.Toast
import com.example.easywaylocation.EasyWayLocation
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.example.easywaylocation.Listener
import com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE
import android.content.Intent
import android.util.Log
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.TOKEN
import com.haja.haja.Utils.USERID
import com.haja.haja.Utils.inTransaction
import com.haja.haja.View.ui.MainCategoriesScreen.MainCategoriesFragment
import kotlinx.android.synthetic.main.app_bar_main.*

class RegisterFragment : Fragment(), Listener {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewModel: RegisterViewModel
    var easyWayLocation: EasyWayLocation? = null
    private var lati: Double? = null
    private var longi: Double? = null

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
        activity?.appBarTitle?.text = resources.getString(R.string.register)
        activity?.categoriesBarBack?.visibility = View.VISIBLE
        activity?.categoriesBarMenu?.visibility = View.GONE
        activity?.catBarSearch?.visibility = View.GONE

        configLocationPremation()
        registerBut.setOnClickListener {
            if (isValidUserInputs()) {
                Log.e("latandLang", "$lati + $longi")
                viewModel.register(userRegistrationInfo()).observe(this, Observer { user ->
                    if (user != null) {
                        if (user.result == true) {
                            user.data?.id?.let { it1 -> SharedPreferenceUtil(context!!).putString(USERID, "$it1") }
                            SharedPreferenceUtil(context!!).putString("userName", user.data?.name.toString())
                            SharedPreferenceUtil(context!!).putString("userPhone", user.data?.mobile.toString())
                            storeUserToken(user.data?.token)
                            makeToast(context!!, resources.getString(R.string.success))
                            goToActivationAccount()
                        } else {
                            makeToast(context!!, user.errorMesage.toString())
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
        Toast.makeText(context!!, "Location ON", Toast.LENGTH_SHORT).show()
        easyWayLocation?.beginUpdates()
        lati = easyWayLocation?.latitude
        longi = easyWayLocation?.longitude
    }

    override fun onPositionChanged() {
        Toast.makeText(context!!, "onPositionChanged", Toast.LENGTH_SHORT).show()

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

    private fun goToActivationAccount() {
        fragmentManager?.inTransaction {
            replace(R.id.mainContainer, MainCategoriesFragment.newInstance())
        }
    }

    private fun storeUserToken(token: String?) {
        SharedPreferenceUtil(context!!).putString(TOKEN , token)
    }
    private fun isValidUserInputs(): Boolean {
        val name = registerName.text.toString()
        val phone = registerPhone.text.toString()
        val pass = `registerPassُ`.text.toString()
        val email = registerEmail.text.toString()
        val confirmPass = `registerConfirmPassُ`.text.toString()

        return if (!isValidUserName(name)) {
            registerName.error = resources.getString(R.string.correct_name)
            false
        } else if (!isValidPhone(phone)) {
            registerPhone.error = resources.getString(R.string.correct_phone)
            false
        } else if (!isValidPassword(pass)) {
            `registerPassُ`.error = resources.getString(R.string.correct_pass)
            false

        } else if (!isValidEmail(email)) {
            registerEmail.error = resources.getString(R.string.correct_email)
            false
        } else if (pass != confirmPass) {
            `registerConfirmPassُ`.error = resources.getString(R.string.mismatch_pass)
            false
        } else
            true
    }

    private fun userRegistrationInfo(): HashMap<String, String> {
        val map = HashMap<String, String>()
        map["name"] = registerName.text.toString()
        map["mobile"] = registerPhone.text.toString()
        map["email"] = registerEmail.text.toString()
        map["password"] = `registerPassُ`.text.toString()
        map["c_password"] = `registerConfirmPassُ`.text.toString()
        map["onesignal_id"] = "test"  //TODO replace onesignal_id when setup onsignal project
        map["latitude"] = lati.toString()
        map["longitude"] = longi.toString()
        return map
    }

    private fun configLocationPremation() {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionRationaleShouldBeShown(
                    permission: com.karumi.dexter.listener.PermissionRequest?,
                    token: PermissionToken?
                ) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

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
