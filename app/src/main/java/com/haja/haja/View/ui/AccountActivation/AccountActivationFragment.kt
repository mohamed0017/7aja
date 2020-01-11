package com.haja.haja.View.ui.AccountActivation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.haja.haja.R
import com.haja.haja.Service.model.UserDataModel
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.TOKEN
import com.haja.haja.Utils.USERID
import com.haja.haja.Utils.inTransaction
import com.haja.haja.View.ui.MainCategoriesScreen.MainCategoriesFragment
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import kotlinx.android.synthetic.main.account_activation_fragment.*
import kotlinx.android.synthetic.main.app_bar_main.*

class AccountActivationFragment : Fragment() {

    companion object {
        fun newInstance(user: UserDataModel?) = AccountActivationFragment().apply {
            arguments = Bundle().apply {
                putParcelable("userData", user)
            }
        }
    }

    private lateinit var viewModel: AccountActivationViewModel
    private var user: UserDataModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.account_activation_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AccountActivationViewModel::class.java)
        activity?.appBarTitle?.text = resources.getString(R.string.activate_account)

        user = arguments?.getParcelable("userData")
        code1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 1)
                    code2.requestFocus()
            }
        })
        code2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 1)
                    code3.requestFocus()
            }
        })
        code3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 1)
                    code4.requestFocus()
            }
        })

        activateAccountBut.setOnClickListener {
            activateAccountProgress.visibility = View.VISIBLE
            activateAccountBut.visibility = View.INVISIBLE
            viewModel.accountActivation(code(), user?.token.toString()).observe(this, Observer { response ->
                activateAccountProgress.visibility = View.GONE
                activateAccountBut.visibility = View.VISIBLE
                if (response != null) {
                    if (response.result == true) {
                        storeUserCredentials()
                        navigateToNextScreen()
                    } else
                        makeToast(context!!, response.errorMesageEn.toString())
                } else
                    makeToast(context!!, resources.getString(R.string.error))
            })
        }

          resendCode.setOnClickListener {
              sendSmsWithCodeToUser(user?.mobile.toString())
          }
    }

    private fun sendSmsWithCodeToUser(mobile: String) {
        viewModel.sendSms(mobile," تم التسجيل في تطبيق حاجة كود التفعيل هو : ${user?.activitationCode}")
            .observe(this, Observer {
                makeToast(context!!, resources.getString(R.string.resend_code_done))
            })
    }
    private fun code(): String {
        return "${code1.text}${code2.text}${code3.text}${code4.text}"
    }

    private fun storeUserCredentials() {
        user?.id?.let { it1 -> SharedPreferenceUtil(context!!).putString(USERID, "$it1") }
        SharedPreferenceUtil(context!!).putString("userName", user?.name.toString())
        SharedPreferenceUtil(context!!).putString("userPhone", user?.mobile.toString())
        SharedPreferenceUtil(context!!).putString(TOKEN, user?.token.toString())
    }

    private fun navigateToNextScreen() {
        fragmentManager?.inTransaction {
            replace(R.id.mainContainer, MainCategoriesFragment.newInstance())
        }
    }
}
