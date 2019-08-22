package com.haja.haja.View.ui.MyFavorites

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
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil
import kotlinx.android.synthetic.main.favorites_fragment.*

class FavoritesFragment : Fragment(), OnProductItemClicked {

    override fun onClick(possion: Int, itemData: ProductData, favIcon: ImageView) {
        viewModel.removeFromFav(itemData.id!!).observe(this, Observer { result ->
            if (result != null) {
                if (result.result == true) {
                    favIcon.setImageResource(R.mipmap.favmdpi)
                    adapter.removeItemFromList(possion)
                    adapter.notifyDataSetChanged()
                } else
                    SnackAndToastUtil.makeToast(context!!, result.errorMesage.toString())
            } else
                SnackAndToastUtil.makeToast(context!!, resources.getString(R.string.error))
        })
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var adapter :ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorites_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favoritesList.layoutManager = LinearLayoutManager(context!!)
        val progress = CustomProgressBar.showProgressBar(context!!)
        adapter = ProductsAdapter(context!!, fragmentManager, this)
        progress.show()
        favoritesList.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
        viewModel.getMyFavorites(169).observe(this, Observer { products ->
            progress.dismiss()
            if (products != null) {
                if (products.result == true) {
                    adapter.setItemList(products.data)
                    adapter.notifyDataSetChanged()
                } else
                    Toast.makeText(context!!, products.errorMesage, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(context!!, resources.getString(R.string.error), Toast.LENGTH_SHORT).show()
        })
    }

}
