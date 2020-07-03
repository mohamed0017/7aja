package com.haja.hja.View.ui.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.haja.hja.R
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.profile_fragment.*
import java.util.HashMap

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance(userId: Int) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putInt("userId", userId)
            }
        }
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        activity?.appBarTitle?.text = resources.getString(R.string.profile)

        getUserInfo()
        profileBut.setOnClickListener {
            profileProgress.visibility = View.VISIBLE
            profileBut.visibility = View.GONE
            viewModel.editProfile(getNewProfileInfo()).observe(viewLifecycleOwner, Observer { result ->
                profileProgress.visibility = View.GONE
                profileBut.visibility = View.VISIBLE
                if (result != null) {
                    makeToast(context!!, result.errorMesage.toString())

                } else
                    makeToast(context!!, resources.getString(R.string.error))
            })
        }

    }

    private fun getUserInfo() {
        val userId = arguments?.getInt("userId", 0)
        viewModel.getProfile(userId!!).observe(viewLifecycleOwner, Observer { result ->
            profileProgress.visibility = View.GONE
            profileBut.visibility = View.VISIBLE
            if (result != null) {
                if (result.result == true) {
                    profileName.setText(result.data?.name.toString())
                    profilePhone.setText(result.data?.mobile.toString())
                    profileEmail.setText(result.data?.email.toString())
                }
            } else
                makeToast(context!!, resources.getString(R.string.error))
        })
    }

    private fun getNewProfileInfo(): HashMap<String, String> {
        val map = HashMap<String, String>()
        map["name"] = profileName.text.toString()
        map["email"] =  profileEmail.text.toString()
        map["mobile"] =  profilePhone.text.toString()
        if(`profilePassُ`.text.length >= 6)
            map["password"] =  `profilePassُ`.text.toString()

        return map
    }

}
