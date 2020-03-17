package com.haja.haja.View.ui.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.haja.haja.R
import com.haja.haja.Service.model.FaqDataModel
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.faq_fragment.*

class FaqFragment : Fragment() {

    companion object {
        fun newInstance() = FaqFragment()
    }

    private lateinit var viewModel: FaqViewModel
    private lateinit var progress : KProgressHUD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.faq_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FaqViewModel::class.java)
        activity?.appBarTitle?.text = resources.getString(R.string.faq)
        progress = CustomProgressBar.showProgressBar(context!!)
        progress.show()
        viewModel.getFaq().observe(this, Observer { result ->
            progress.dismiss()
            if (result != null) {
                faqList.layoutManager = LinearLayoutManager(context!!)
                faqList.adapter = FaqAdapter(result.faqData as ArrayList<FaqDataModel?>?)
            } else
                makeToast(context!!, resources.getString(R.string.error))
        })
    }

    override fun onStop() {
        super.onStop()
        progress.dismiss()
    }
}
