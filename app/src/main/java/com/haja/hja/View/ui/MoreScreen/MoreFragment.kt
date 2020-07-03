package com.haja.hja.View.ui.MoreScreen

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.haja.hja.R
import com.haja.hja.Service.model.SocialMediasModel
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.USERID
import com.haja.hja.Utils.inTransaction
import com.haja.hja.View.ui.ChatsScreen.ChatsFragment
import com.haja.hja.View.ui.ContactUs.ContactUsFragment
import com.haja.hja.View.ui.DelegatesScreen.DelegatesFragment
import com.haja.hja.View.ui.LoginScreen.LoginFragment
import com.haja.hja.View.ui.MyAdsScreen.MyAdsFragment
import com.haja.hja.View.ui.profile.ProfileFragment
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.balance_dialog.*
import kotlinx.android.synthetic.main.more_fragment.*

class MoreFragment : Fragment() {

    companion object {
        fun newInstance() = MoreFragment()
    }

    private var viewModel: MoreViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.more_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.appBarTitle?.text = resources.getString(R.string.more)
        activity?.categoriesBarBack?.visibility = View.GONE
        activity?.categoriesBarMenu?.visibility = View.VISIBLE
        activity?.catBarSearch?.visibility = View.GONE

        viewModel = ViewModelProviders.of(this).get(MoreViewModel::class.java)

        val userId = SharedPreferenceUtil(context!!).getString(USERID, "0")?.toInt()
        if (userId == 0)
            myProfile.visibility = View.GONE
        else {
            myProfile.visibility = View.VISIBLE
            myProfile.setOnClickListener {
                fragmentManager?.inTransaction {
                    replace(
                        R.id.mainContainer,
                        ProfileFragment.newInstance(userId!!.toInt())
                    ).addToBackStack("MoreFragment")
                }
            }
        }
        myAds.setOnClickListener {
            if (userId == 0) {
                makeToast(context!!, resources.getString(R.string.login_first))
                fragmentManager?.inTransaction {
                    replace(
                        R.id.mainContainer,
                        LoginFragment.newInstance()
                    ).addToBackStack("LoginFragment")
                }
            } else
                fragmentManager?.inTransaction {
                    replace(
                        R.id.mainContainer,
                        MyAdsFragment.newInstance()
                    ).addToBackStack("MoreFragment")
                }
        }

        delegates.setOnClickListener {
            fragmentManager?.inTransaction {
                replace(
                    R.id.mainContainer,
                    DelegatesFragment.newInstance()
                ).addToBackStack("MoreFragment")
            }
        }
        moreMessages.setOnClickListener {
            if (userId != 0)
                fragmentManager?.inTransaction {
                    replace(
                        R.id.mainContainer,
                        ChatsFragment.newInstance()
                    ).addToBackStack("MoreFragment")
                }
            else {
                makeToast(context!!, resources.getString(R.string.login_first))
                fragmentManager?.inTransaction {
                    replace(
                        R.id.mainContainer,
                        LoginFragment.newInstance()
                    ).addToBackStack("LoginFragment")
                }
            }

        }
        if (userId != 0)
            getUserInfo(userId!!)
        getContactDetails()
    }

    private fun getContactDetails() {
        viewModel?.getContactDetails()?.observe(viewLifecycleOwner, Observer { result ->
            if (result != null) {
                initSocialMedia(result.data?.socialMedias)
                supportUS.setOnClickListener {
                    fragmentManager?.inTransaction {
                        replace(
                            R.id.mainContainer,
                            ContactUsFragment.newInstance(result.data?.contactDetails)
                        )
                            .addToBackStack("MoreFragment")
                    }
                }
            } else
                makeToast(context!!, resources.getString(R.string.error))
        })
    }

    private fun initSocialMedia(socialMedias: List<SocialMediasModel?>?) {
        socials.layoutManager = GridLayoutManager(context, 3)
        socials.adapter = SocialMediaAdapter(context!!,socialMedias)
    }

    private fun openBalanceDialog(adsCount: String?) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.balance_dialog)
        dialog.adsBalance.text = adsCount
        dialog.ok.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun getUserInfo(userId: Int) {
        val progress = CustomProgressBar.showProgressBar(context!!)
        progress.show()
        viewModel?.getProfile(userId)?.observe(viewLifecycleOwner, Observer { result ->
            progress.dismiss()
            if (result != null) {
                if (result.result == true) {
                    profileUserName.text = result.data?.name
                    profilePhone.text = result.data?.mobile
                }
            } else
                makeToast(context!!, resources.getString(R.string.error))
        })
    }
}
