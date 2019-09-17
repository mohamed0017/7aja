package com.haja.haja.View.ui.ChatsScreen

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.haja.haja.R
import com.haja.haja.View.ui.NotificationsHistory.NotificationsAdapter
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.chats_fragment.*

class ChatsFragment : Fragment() {

    companion object {
        fun newInstance() = ChatsFragment()
    }

    private lateinit var viewModel: ChatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chats_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.categoriesBarBack?.visibility = View.VISIBLE
        activity?.categoriesBarMenu?.visibility = View.GONE

        viewModel = ViewModelProviders.of(this).get(ChatsViewModel::class.java)
    }

    override fun onResume() {

        activity?.appBarTitle?.text = resources.getString(R.string.messages)

        val progress = CustomProgressBar.showProgressBar(context!!)
        progress.show()
        chatsList.layoutManager = LinearLayoutManager(context)
        viewModel.getAllChats()?.observe(this, Observer { chats ->
            progress.dismiss()
            if (chats != null) {
                chatsList.adapter = ChatsAdapter(context!!,chats.chatsData)
            } else
                SnackAndToastUtil.makeToast(context!!, resources.getString(R.string.error))
        })
        super.onResume()
    }
}
