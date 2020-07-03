package com.haja.hja.View.ui.Products

import android.nfc.tech.MifareUltralight.PAGE_SIZE
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
import androidx.recyclerview.widget.RecyclerView
import com.haja.hja.OnProductItemClicked
import com.haja.hja.R
import com.haja.hja.Service.model.ProductData
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.USERID
import com.haja.hja.Utils.inTransaction
import com.haja.hja.View.ui.LoginScreen.LoginFragment
import com.haja.hja.View.ui.Products.PaginationListener.Companion.PAGE_START
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.products_fragment.*

class ProductsFragment : Fragment(), OnProductItemClicked {

    companion object {
        fun newInstance(categoryId: Int, name: String?, isAll: Boolean = false) =
            ProductsFragment().apply {
                arguments = Bundle().apply {
                    putInt("categoryId", categoryId)
                    putString("categoryName", name)
                    putBoolean("isAll", isAll)
                }
            }
    }

    private lateinit var viewModel: ProductsViewModel
    private lateinit var adapter: ProductsAdapter

    private var currentPage = PAGE_START
    private var isLastPage = false
    private var totalPage = 10
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.products_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val categoryId = arguments!!.getInt("categoryId")
        val categoryName = arguments!!.getString("categoryName")
        activity?.appBarTitle?.text = categoryName
        activity?.categoriesBarBack?.visibility = View.VISIBLE
        activity?.categoriesBarMenu?.visibility = View.GONE

        val userId = SharedPreferenceUtil(context!!).getString(USERID, "0")?.toInt()
        val progress = CustomProgressBar.showProgressBar(context!!)
        progress.show()
        viewModel = ViewModelProviders.of(this).get(ProductsViewModel::class.java)
        adapter = ProductsAdapter(context!!, fragmentManager, this)

        val layoutManager = LinearLayoutManager(context!!)
        productsList.layoutManager = layoutManager
        productsList.adapter = adapter

        productsList.addOnScrollListener(object : PaginationListener(layoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage++
                viewModel.page = currentPage
                getProducts(categoryId, userId!!, progress)
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })

        getProducts(categoryId, userId!!, progress)
    }

    fun getProducts(
        categoryId: Int,
        userId: Int,
        progress: KProgressHUD
    ) {
        viewModel.getProducts(categoryId, userId, arguments?.getBoolean("isAll") ?: false)
            .observe(this, Observer { products ->
                progress.dismiss()
                if (products != null) {
                    if (products.result == true) {
                        adapter.setItemList(products.data?.data as List<ProductData>?)
                        adapter.notifyDataSetChanged()
                    } else
                        Toast.makeText(context!!, products.errorMesage, Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(
                        context!!,
                        resources.getString(R.string.error),
                        Toast.LENGTH_SHORT
                    ).show()
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
                replace(
                    R.id.mainContainer,
                    LoginFragment.newInstance()
                ).addToBackStack("LoginFragment")
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

abstract class PaginationListener(val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    companion object {
        val PAGE_START = 1

    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.getChildCount()
        val totalItemCount = layoutManager.getItemCount()
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE
            ) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
}