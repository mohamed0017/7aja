package com.haja.haja.View.ui.LoginScreen

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.haja.haja.R
import com.haja.haja.Utils.*
import com.haja.haja.View.ui.MainCategoriesScreen.MainCategoriesFragment
import com.haja.haja.View.ui.Register.RegisterFragment
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import kotlinx.android.synthetic.main.activity_main_categories.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.forget_pass_dialog.*
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
       // activity?.bottomNavigation?.visibility = View.GONE
        activity?.appBarTitle?.text = resources.getString(R.string.login)
        activity?.categoriesBarBack?.visibility = View.VISIBLE
        activity?.categoriesBarMenu?.visibility = View.GONE
        activity?.catBarSearch?.visibility = View.GONE

        loginPhoneCode.keyListener = null
        loginBut.setOnClickListener {
                login()
        }

        registerLogin.setOnClickListener {
            fragmentManager?.inTransaction {
                replace(R.id.mainContainer, RegisterFragment.newInstance())
            }
        }

        resetPassword.setOnClickListener {
            showForgetPassDialog()
        }
    }

    private fun login() {
        val progress = CustomProgressBar.showProgressBar(context!!)
        progress.show()
        viewModel.login(getLoginCradentioals()).observe(this, Observer { user ->
            progress.dismiss()
            if (user != null) {
                if (user.result == true) {
                    storeUserToken(user.data?.token)
                    user.data?.id?.let { it1 -> SharedPreferenceUtil(context!!).putString(USERID, "$it1") }
                    SharedPreferenceUtil(context!!).putString("userName", user.data?.name.toString())
                    SharedPreferenceUtil(context!!).putString("userPhone", user.data?.mobile.toString())
                    //makeToast(context!!, resources.getString(R.string.success))
                    activity?.nav_view?.menu?.findItem(R.id.nav_logOut)?.isVisible = true
                    goToActivationAccount()
                } else {
                    CustomDialog.showDialog(context!!, user.errorMesage.toString())
                }
            } else
                makeToast(context!!, resources.getString(R.string.error))
        })
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
        val phone = loginPhone.text.toString()
        val pass = loginPass.text.toString()
        return if (!ValidationUtils.isValidPassword(pass)) {
            loginPass.error = resources.getString(R.string.correct_pass)
            false
        } else if (!ValidationUtils.isValidEmail(phone)) {
            loginPhone.error = resources.getString(R.string.correct_email)
            false
        } else
            true
    }

    private fun getLoginCradentioals(): HashMap<String, String> {
        val map = HashMap<String, String>()
        map["user_name"] = "${loginPhone.text}"
      //  map["user_name"] = "${loginPhone.text}" // TODO just for debug
        map["password"] = loginPass.text.toString()
        return map
    }

    private fun showForgetPassDialog() {
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.forget_pass_dialog)
        dialog.show()

        dialog.forgerPassBut.setOnClickListener {
            val phone = dialog.forgetProgressPhone.text.toString()
            if (phone.isNotEmpty()) {
                dialog.forgetProgress.visibility = View.VISIBLE
                dialog.forgerPassBut.visibility = View.GONE
                viewModel.forgetPass("965$phone").observe(this, Observer { result ->
                    dialog.forgetProgress.visibility = View.GONE
                    dialog.forgerPassBut.visibility = View.VISIBLE
                    if (result != null) {
                        dialog.dismiss()
                        makeToast(context!!, result.errorMesageEn.toString())
                    } else
                        makeToast(context!!, resources.getString(R.string.error))
                })
            } else
                dialog.forgetProgressPhone.error = resources.getString(R.string.requird)
        }
    }
}
