package com.haja.haja.View.ui.LoginScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.haja.haja.R
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.TOKEN
import com.haja.haja.Utils.ValidationUtils
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
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

        val progress = CustomProgressBar.showProgressBar(context!!)
        loginBut.setOnClickListener {
            if (isValidUserInputs()) {
                progress.show()
                viewModel.login(getLoginCradentioals()).observe(this, Observer { user ->
                    progress.dismiss()
                    if (user != null) {
                        if (user.success != null && user.errorMesage == null) {
                            storeUserToken(user.success.token)
                            makeToast(context!!, resources.getString(R.string.success))
                        } else {
                            makeToast(context!!, resources.getString(R.string.login_message))
                        }
                    } else
                        makeToast(context!!, resources.getString(R.string.error))
                })
            }

        }
    }

    private fun storeUserToken(token: String?) {
        SharedPreferenceUtil(context!!).putString(TOKEN , token)
    }

    private fun isValidUserInputs(): Boolean {
        val phone = loginEmail.text.toString()
        val pass = loginPass.text.toString()
        return if (!ValidationUtils.isValidPassword(pass)) {
            loginPass.error = resources.getString(R.string.correct_pass)
            false
        } else if (!ValidationUtils.isValidEmail(phone)) {
            loginEmail.error = resources.getString(R.string.correct_email)
            false
        } else
            true
    }

    private fun getLoginCradentioals(): HashMap<String, String> {
        val map = HashMap<String, String>()
        map["email"] = loginEmail.text.toString()
        map["password"] = loginPass.text.toString()
        return map
    }

}
