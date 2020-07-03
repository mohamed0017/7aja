package com.haja.hja.View.ui.MainCategoriesScreen

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.haja.hja.R
import com.haja.hja.Service.ApiService
import com.haja.hja.Service.model.AdsModel
import com.haja.hja.Service.model.ImgesDataModel
import com.haja.hja.model.CategoriesData
import com.haja.hja.model.CategoriesModel
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar.Companion.showProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import com.kaopiz.kprogresshud.KProgressHUD
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_ad.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.main_categories_fragment.*

class MainCategoriesFragment : Fragment() {

    companion object {
        fun newInstance() = MainCategoriesFragment()
    }

    private var requestsCount = 0
    private var viewModel: MainCategoriesViewModel? = null
    private var categories = ArrayList<CategoriesData>()
    private var progress: KProgressHUD? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_categories_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // activity?.bottomNavigation?.visibility = View.VISIBLE
        activity?.appBarTitle?.text = resources.getString(R.string.categories)
        activity?.categoriesBarBack?.visibility = View.GONE
        activity?.categoriesBarMenu?.visibility = View.VISIBLE
        activity?.catBarSearch?.visibility = View.VISIBLE

        progress = showProgressBar(context!!)
        if (viewModel == null)
            viewModel = ViewModelProviders.of(this).get(MainCategoriesViewModel::class.java)

        if (viewModel?.getMainCategories() == null) {
            progress?.show()
            // get main categories
            viewModel?.getCategories(0)?.observe(this, Observer {
                if (it != null) {
                    if (!it.data.isNullOrEmpty())
                        categories = it.data as ArrayList<CategoriesData>
                    getChildCategories(it)
                    getChildrenAds(it)
                }
            })
        } else
            initRecycler()

        initSliderImages()
    }

    private fun initSliderImages() {
        mainImageSlider.startAutoCycle()
        mainImageSlider.setIndicatorAnimation(IndicatorAnimations.WORM)
        mainImageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)

        viewModel?.getMainSliderImages()?.observe(this, Observer { images ->
            if (images != null) {
                if (images.result == true)
                    mainImageSlider.sliderAdapter =
                        MainSliderAdapter(context!!, images.imgesData as List<ImgesDataModel>)
            } else
                makeToast(context!!, resources.getString(R.string.error))
        })
    }

    private fun setupStartupAdDialog(ad: AdsModel) {
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.activity_ad)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.closeAd.setOnClickListener {
            dialog.dismiss()
        }
        Picasso.get().load(ApiService.IMAGEBASEURL + "${ad.data?.get(0)?.img}")
            .placeholder(resources.getDrawable(R.drawable.placeholder))
            .error(resources.getDrawable(R.drawable.placeholder)).into(dialog.startupAdImg)
        dialog.show()
    }

    private fun getStartupAd() {
        viewModel?.getStartupAd()?.observe(this, Observer { ad ->
            if (ad != null) {
                if (ad.result == true)
                    setupStartupAdDialog(ad)
                else
                    Toast.makeText(context!!, ad.errorMessage, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(
                    context!!,
                    resources.getString(R.string.error),
                    Toast.LENGTH_SHORT
                ).show()
        })
    }

    private fun getChildCategories(parentCategories: CategoriesModel?) {
        Log.i("getChildCategories", "two00")
        for (i in parentCategories?.data?.indices!!) {
            Log.i("getChildCategories", "oo")
            viewModel?.getCategories(parentCategories.data[i]?.id)?.observe(this, Observer {
                if (it != null) {
                    categories[i].childCategories = it.data
                    requestsCount++
                  //  Log.i("sub_categories/size", it.data?.size.toString())
                   // Log.i("requestsCount/size", requestsCount.toString())
                    if ((parentCategories.data.size) * 2 == requestsCount) {
                        progress?.dismiss()
                     //   Log.i("cat/competed", "$requestsCount")
                        initRecycler()
                    }
                } else {
                    progress?.dismiss()
                 //   Log.i("getChildCategories", "getChildCategories")
                }
            })
        }
    }

    private fun getChildrenAds(parentCategories: CategoriesModel?) {
        Log.i("getChildrenAds", "two00")
        for (i in parentCategories?.data?.indices!!) {
            Log.i("getChildrenAds/i", "$i")
            viewModel?.getAds(parentCategories.data[i]?.id)?.observe(this, Observer {
                if (it != null) {
                    categories[i].childAds = it.data
                    requestsCount++
                    Log.i("Ads/size", it.data?.size.toString())
                    if ((parentCategories.data.size) * 2 == requestsCount) {
                        progress?.dismiss()
                        Log.i("ads/competed", "$requestsCount")
                        initRecycler()
                    }
                } else {
                    progress?.dismiss()
                    Log.i("getChildrenAds", "getChildCategories")
                }
            })
        }
    }

    private fun initRecycler() {

        rv_parent.apply {
            layoutManager = object : LinearLayoutManager(
                context
            ) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            if (viewModel?.getMainCategories() == null) {
                viewModel?.setMainCategories(categories)
                Log.i("kghmghjmjhjh", "gkmikghm")

            } else {
                Log.i("gnhgmnvb", "gkmikghm")
                categories = viewModel?.getMainCategories()!!
            }
            adapter = MainCategoriesAdapter(categories, context, fragmentManager)
        }

    }

    override fun onPause() {
        super.onPause()
        progress?.dismiss()
    }

    override fun onStop() {
        super.onStop()
        progress?.dismiss()
        requestsCount = 0
    }
}
