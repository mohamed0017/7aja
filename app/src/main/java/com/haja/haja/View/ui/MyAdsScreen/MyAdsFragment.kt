package com.haja.haja.View.ui.MyAdsScreen

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
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.my_ads_fragment.*

class MyAdsFragment : Fragment(), OnProductItemClicked {

    companion object {
        fun newInstance() = MyAdsFragment()
    }

    private lateinit var viewModel: MyAdsViewModel
    private lateinit var adapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_ads_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.appBarTitle?.text = resources.getString(R.string.my_ads)
        activity?.categoriesBarBack?.visibility = View.VISIBLE
        activity?.categoriesBarMenu?.visibility = View.GONE

        viewModel = ViewModelProviders.of(this).get(MyAdsViewModel::class.java)
        adapter = ProductsAdapter(context!!, fragmentManager, this)

        myProductsList.layoutManager = LinearLayoutManager(context!!)
        myProductsList.adapter = adapter
        viewModel.getMyProducts().observe(this, Observer { products ->
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

    override fun onClick(possion: Int, itemData: ProductData, favIcon: ImageView) {
        viewModel.removeProduct(itemData.id!!).observe(this, Observer { result ->
            if (result != null) {
                if (result.result == true) {
                    adapter.removeItemFromList(possion)
                    adapter.notifyDataSetChanged()
                }
            } else
                makeToast(context!!, resources.getString(R.string.error))
        })
    }
}
