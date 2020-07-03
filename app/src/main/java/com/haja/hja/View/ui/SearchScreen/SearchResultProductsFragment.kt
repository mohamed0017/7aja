package com.haja.hja.View.ui.SearchScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.haja.hja.OnProductItemClicked
import com.haja.hja.R
import com.haja.hja.Service.model.ProductData
import com.haja.hja.Service.model.SearchRequest
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.USERID
import com.haja.hja.Utils.inTransaction
import com.haja.hja.View.ui.LoginScreen.LoginFragment
import com.haja.hja.View.ui.Products.ProductsAdapter
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.search_result_products_fragment.*

class SearchResultProductsFragment : Fragment(), OnProductItemClicked {

    companion object {
        fun newInstance(searchData: SearchRequest) = SearchResultProductsFragment().apply {
            arguments = Bundle().apply {
                putParcelable("searchData", searchData)
            }
        }
    }

    private lateinit var viewModel: SearchResultProductsViewModel
    private lateinit var adapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_result_products_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.appBarTitle?.text = resources.getString(R.string.search_but_title)
        activity?.categoriesBarBack?.visibility = View.VISIBLE
        activity?.categoriesBarMenu?.visibility = View.GONE

        val progress = CustomProgressBar.showProgressBar(context!!)
        progress.show()

        viewModel = ViewModelProviders.of(this).get(SearchResultProductsViewModel::class.java)
        adapter = ProductsAdapter(context!!, fragmentManager, this)

        productsSearchResultList.layoutManager = LinearLayoutManager(context!!)
        productsSearchResultList.adapter = adapter

        val searchData = arguments?.getParcelable("searchData") as SearchRequest
        viewModel.searchProducts(searchData).observe(this, Observer { products ->
            progress.dismiss()
            if (products != null) {
                if (products.result == true) {
                    adapter.setItemList(products.data?.data as List<ProductData>?)
                    adapter.notifyDataSetChanged()
                } else
                    Toast.makeText(context!!, products.errorMesage, Toast.LENGTH_SHORT).show()
            } else
                makeToast(context!!, resources.getString(R.string.error))
        })

    }

    override fun onClick(
        possion: Int,
        itemData: ProductData,
        favIcon: ImageView
    ) {
        val userId = SharedPreferenceUtil(context!!).getString(USERID, "0")?.toInt()
        if (userId == 0) {
            makeToast(context!!, resources.getString(R.string.login_first))
            fragmentManager?.inTransaction {
                replace(R.id.mainContainer, LoginFragment.newInstance()).addToBackStack("LoginFragment")
            }
        } else {
            if (itemData.isFavorite == null) {
                viewModel.addToFav(itemData.id!!).observe(this, Observer { result ->
                    if (result != null) {
                        if (result.result == true) {
                            makeToast(context!!, resources.getString(R.string.added))
                            favIcon.setImageResource(R.mipmap.fav_hov_1mdpi)
                            itemData.isFavorite = result.insertID
                        } else
                            makeToast(context!!, result.errorMesage.toString())
                    } else
                        makeToast(context!!, resources.getString(R.string.error))
                })
            } else {
                viewModel.removeFromFav(itemData.isFavorite!!).observe(this, Observer { result ->
                    if (result != null) {
                        if (result.result == true) {
                            favIcon.setImageResource(R.mipmap.favmdpi)
                            itemData.isFavorite = null
                        } else
                            makeToast(context!!, result.errorMesage.toString())
                    } else
                        makeToast(context!!, resources.getString(R.string.error))
                })
            }
        }

    }
}
