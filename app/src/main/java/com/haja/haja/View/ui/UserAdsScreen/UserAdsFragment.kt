package com.haja.haja.View.ui.UserAdsScreen

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.haja.haja.OnProductItemClicked
import com.haja.haja.R
import com.haja.haja.Service.model.ProductData
import com.haja.haja.View.ui.Products.ProductsAdapter
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.user_ads_fragment.*

class UserAdsFragment : Fragment(), OnProductItemClicked {

    override fun onClick(possion: Int, itemData: ProductData, favIcon: ImageView) {

    }

    companion object {
        fun newInstance(userId: Int?) = UserAdsFragment().apply {
            arguments = Bundle().apply {
                putInt("userID", userId!!)
            }
        }
    }

    private lateinit var viewModel: UserAdsViewModel
    private lateinit var adapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_ads_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserAdsViewModel::class.java)
        userAdsTitleBarName?.text = resources.getString(R.string.ads)
        userAdsBack.setOnClickListener {
            activity?.onBackPressed()
        }
        adapter = ProductsAdapter(context!!, fragmentManager, this)

        userAdsList.layoutManager = LinearLayoutManager(context!!)
        userAdsList.adapter = adapter
        viewModel.getMyProducts(arguments?.getInt("userID")!!).observe(this, Observer { products ->
            if (products != null) {
                if (products.result == true) {
                    adapter.setItemList(products.data?.data as List<ProductData>?)
                    adapter.notifyDataSetChanged()
                } else
                    Toast.makeText(context!!, products.errorMesage, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(context!!, resources.getString(R.string.error), Toast.LENGTH_SHORT).show()
        })
    }

}
