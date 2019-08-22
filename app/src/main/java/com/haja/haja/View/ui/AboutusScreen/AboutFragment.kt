package com.haja.haja.View.ui.AboutusScreen

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer

import com.haja.haja.R
import com.haja.haja.Service.model.StaticPagesModel
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar.Companion.showProgressBar
import kotlinx.android.synthetic.main.about_fragment.*
import kotlinx.android.synthetic.main.app_bar_main.*

class AboutFragment : Fragment() {

    companion object {
        fun newInstance(type: Int) = AboutFragment().apply {
            arguments = Bundle().apply {
                putInt("contentType", type)
            }
        }
    }

    private lateinit var viewModel: AboutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AboutViewModel::class.java)
        val contentType = arguments?.getInt("contentType")
        val progress = showProgressBar(context!!)
        progress.show()
        Log.i("contentType", contentType.toString())
        viewModel.getPageContent(contentType!!).observe(this, Observer { content ->
            progress.dismiss()
            if (content != null) {
                if (content.result == true)
                    setPageData(content)
                else
                    Toast.makeText(context!!, content.errorMesage, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(context!!, resources.getString(R.string.error), Toast.LENGTH_SHORT).show()
        })
    }

    private fun setPageData(content: StaticPagesModel?) {
        activity?.catBarTitle?.text = content?.data?.name.toString()
        contentTitle.text = content?.data?.name.toString()
        contentDetails.text = content?.data?.details.toString()
    }

}
