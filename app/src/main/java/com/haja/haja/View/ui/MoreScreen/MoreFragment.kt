package com.haja.haja.View.ui.MoreScreen

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.haja.haja.R
import com.haja.haja.Service.ApiService.Companion.IMAGEBASEURL
import com.haja.haja.Service.model.SocialMediasModel
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.USERID
import com.haja.haja.Utils.inTransaction
import com.haja.haja.View.ui.ChatsScreen.ChatsFragment
import com.haja.haja.View.ui.ContactUs.ContactUsFragment
import com.haja.haja.View.ui.DelegatesScreen.DelegatesFragment
import com.haja.haja.View.ui.LoginScreen.LoginFragment
import com.haja.haja.View.ui.MyAdsScreen.MyAdsFragment
import com.haja.haja.View.ui.profile.ProfileFragment
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import com.squareup.picasso.Picasso
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
        Picasso.get().load(IMAGEBASEURL + socialMedias?.get(0)?.img).into(facebookImg)
        Picasso.get().load(IMAGEBASEURL + socialMedias?.get(1)?.img).into(twitterImg)
        Picasso.get().load(IMAGEBASEURL + socialMedias?.get(2)?.img).into(instagramImg)

        facebook.setOnClickListener {
            val url = socialMedias?.get(0)?.urlL
            if (url?.contains("http")!!) {
                val openUrlIntent = Intent(ACTION_VIEW, Uri.parse(url))
                openUrlIntent.flags = FLAG_ACTIVITY_NEW_TASK
                activity?.startActivity(openUrlIntent)
            }
        }

        youtube.setOnClickListener {
            val url = socialMedias?.get(1)?.urlL
            if (url?.contains("http")!!) {
                val openUrlIntent = Intent(ACTION_VIEW, Uri.parse(url))
                openUrlIntent.flags = FLAG_ACTIVITY_NEW_TASK
                context?.startActivity(openUrlIntent)
            }
        }

        instagram.setOnClickListener {
            val url = socialMedias?.get(2)?.urlL
            if (url?.contains("http")!!) {
                val openUrlIntent = Intent(ACTION_VIEW, Uri.parse(url))
                openUrlIntent.flags = FLAG_ACTIVITY_NEW_TASK
                startActivity(openUrlIntent)
            }
        }
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
