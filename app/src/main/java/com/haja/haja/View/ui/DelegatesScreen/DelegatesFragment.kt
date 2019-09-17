package com.haja.haja.View.ui.DelegatesScreen

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.haja.haja.R
import com.haja.haja.Service.model.DelegatesdataModel
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.delegates_fragment.*

class DelegatesFragment : Fragment() {

    companion object {
        fun newInstance() = DelegatesFragment()
    }

    private lateinit var viewModel: DelegatesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.delegates_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.categoriesBarBack?.visibility = View.VISIBLE
        activity?.categoriesBarMenu?.visibility = View.GONE

        activity?.appBarTitle?.text = resources.getString(R.string.delegates)

        viewModel = ViewModelProviders.of(this).get(DelegatesViewModel::class.java)
        delegatesList.layoutManager = GridLayoutManager(context!!,2)
        val progress = CustomProgressBar.showProgressBar(context!!)
        progress.show()
        viewModel.getDelegates().observe(this, Observer { delegates ->
            progress.dismiss()
            if (delegates != null) {
                delegatesList.adapter = DelegatesAdapter(context!! ,
                    delegates.delegatesdata?.delegatesdata as ArrayList<DelegatesdataModel>
                )
            } else
                makeToast(context!!, resources.getString(R.string.error))
        })
    }

}
