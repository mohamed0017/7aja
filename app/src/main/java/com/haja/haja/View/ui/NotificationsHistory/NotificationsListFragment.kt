package com.haja.haja.View.ui.NotificationsHistory

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.haja.haja.R
import com.haja.haja.Service.model.NotificationData
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.notifications_list_fragment.*

class NotificationsListFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationsListFragment()
    }

    private lateinit var viewModel: NotificationsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notifications_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.appBarTitle?.text = resources.getString(R.string.notifications)
        activity?.categoriesBarBack?.visibility = View.GONE
        activity?.categoriesBarMenu?.visibility = View.VISIBLE
        activity?.catBarSearch?.visibility = View.GONE

        val progress = CustomProgressBar.showProgressBar(context!!)
        progress.show()
        notificationsList.layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProviders.of(this).get(NotificationsListViewModel::class.java)
        viewModel.getNotifications().observe(this, Observer { notifications ->
            progress.dismiss()
            if (notifications != null) {
                notificationsList.adapter = NotificationsAdapter(notifications.data as ArrayList<NotificationData?>?)
            } else
                makeToast(context!!, resources.getString(R.string.error))
        })
    }

}
