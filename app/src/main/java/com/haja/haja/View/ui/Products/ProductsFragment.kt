package com.haja.haja.View.ui.Products

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
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import kotlinx.android.synthetic.main.products_fragment.*

class ProductsFragment : Fragment(), OnProductItemClicked {

    override fun onClick(
        possion: Int,
        itemData: ProductData,
        favIcon: ImageView
    ) {
        if (itemData.isFavorite == null) {
            viewModel.addToFav(itemData.id!!).observe(this, Observer { result ->
                if (result != null) {
                    if (result.result == true) {
                        makeToast(context!!, resources.getString(R.string.added))
                        favIcon.setImageResource(R.mipmap.fav_hov_1mdpi)
                    } else
                        makeToast(context!!, result.errorMesage.toString())
                } else
                    makeToast(context!!, resources.getString(R.string.error))
            })
        } else {
            viewModel.removeFromFav(itemData.isFavorite).observe(this, Observer { result ->
                if (result != null) {
                    if (result.result == true) {
                        favIcon.setImageResource(R.mipmap.favmdpi)
                    } else
                        makeToast(context!!, result.errorMesage.toString())
                } else
                    makeToast(context!!, resources.getString(R.string.error))
            })
        }
    }

    companion object {
        fun newInstance(categoryId: Int) = ProductsFragment().apply {
            arguments = Bundle().apply {
                putInt("categoryId", categoryId)
            }
        }
    }

    private lateinit var viewModel: ProductsViewModel
    private lateinit var adapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.products_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val categoryId = arguments!!.getInt("categoryId")
        viewModel = ViewModelProviders.of(this).get(ProductsViewModel::class.java)
        adapter = ProductsAdapter(context!!, fragmentManager, this)

        productsList.layoutManager = LinearLayoutManager(context!!)
        productsList.adapter = adapter
        viewModel.getProducts(categoryId).observe(this, Observer { products ->
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
