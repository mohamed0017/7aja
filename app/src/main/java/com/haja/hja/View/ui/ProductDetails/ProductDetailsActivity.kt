package com.haja.hja.View.ui.ProductDetails

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.haja.hja.Service.model.ProductData
import com.haja.hja.View.Adapter.AttributsAdapter
import com.haja.hja.View.Adapter.SliderAdapterExample
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import kotlinx.android.synthetic.main.activity_product_details.*
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import com.haja.hja.R
import com.haja.hja.Service.ApiService.Companion.IMAGEBASEURL
import com.haja.hja.Utils.*
import com.haja.hja.Utils.ShareImage.shareImage
import com.haja.hja.View.ui.ChatScreen.ChatActivity
import com.haja.hja.View.ui.MainCategoriesActivity.MainCategoriesActivity
import com.haja.hja.View.ui.UserAdsScreen.UserAdsFragment
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.report_dialog.*

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: ProductDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val progress = CustomProgressBar.showProgressBar(this)
        progress.show()
        val productDetails = intent.extras?.getInt("productId", 0)
        viewModel = ViewModelProviders.of(this).get(ProductDetailsViewModel::class.java)
        viewModel.getSingleProduct(productDetails!!).observe(this, Observer { result ->
            progress.dismiss()
            if (result != null) {
                if (result.result == true && result.data != null) {
                    val product = result.data
                    title = product.name
                    viewModel.productView(product.id!!).observe(this, Observer { viewsResult ->
                        // product.numViews++
                        var views = product.numViews
                        if (views != null) {
                            views++
                            productViews.text = views.toString()
                        }
                    })
                    initUserInfo(product)
                    var date = product.createdAt
                    date = date?.substringBefore(" ")
                    productName.text = product.name
                    productDate.text = date
                    productCat.text = product.catName
                    productViews.text = product.numViews.toString()
                    productDescription.text = product.description
                    productItemCost.text = "${product.price.toString()}  ${resources.getString(R.string.price_unit)}"
                    productAttributes.layoutManager = LinearLayoutManager(this)
                    productAttributes.isNestedScrollingEnabled = false
                    if (!product.attributes.isNullOrEmpty())
                        productAttributes.adapter = AttributsAdapter(product.attributes)

                    if (!product.imgs.isNullOrEmpty())
                        imageSlider.sliderAdapter = SliderAdapterExample(this, product.imgs)
                    if (product.isFavorite != null)
                        productFav.setImageResource(R.mipmap.fav_hov_1mdpi)
                    // imageSlider.startAutoCycle()
                    imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM)
                    imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)

                    initButtons(product)
                }
            }
        })

    }

    private fun initUserInfo(product: ProductData) {
        productUserName.text = product.user?.name.toString()
        if (product.user?.img != null) {
            Picasso.get().load(product.user?.img)
                .error(resources.getDrawable(R.drawable.placeholder)).into(proUserImg)
        }
        productShowMoreAds.setOnClickListener {
            if (product.user?.id != null)
            supportFragmentManager.inTransaction {
                replace(R.id.productContainer, UserAdsFragment.newInstance(product.user?.id)).addToBackStack("productDetails")
            }
        }

        productDetailsBackBut.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initButtons(product: ProductData) {
        productCall.setOnClickListener {
            val phone = product.mobile
            if (phone != null) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                startActivity(intent)
            }
        }
        productFav.setOnClickListener {
            val userId = SharedPreferenceUtil(this).getString(USERID, "0")?.toInt()
            if (userId == 0) {
                makeToast(this, resources.getString(R.string.login_first))
            } else
                addProductToFav(product)
        }
        productReport.setOnClickListener {
            val userId = SharedPreferenceUtil(this).getString(USERID, "0")?.toInt()
            if (userId == 0) {
                makeToast(this, resources.getString(R.string.login_first))

            } else
                showDialog(product)
        }
        productShare.setOnClickListener {
            if (!product.imgs.isNullOrEmpty())
                shareAd(product.imgs[0]?.img!!, product)
        }
        productMessage.setOnClickListener {
            val userId = SharedPreferenceUtil(this).getString(USERID, "0")?.toInt()
            if (userId == 0) {
                makeToast(this, resources.getString(R.string.login_first))
            } else{
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("user2_id" , product.user?.id.toString())
                intent.putExtra("user2_name" , product.user?.name.toString())
                startActivity(intent)
            }
        }
    }

    private fun shareAd(imgUrl: String, product: ProductData) {
        shareImage(IMAGEBASEURL + imgUrl, this, product)
    }

    private fun sendProductReport(
        product: ProductData,
        message: String,
        dialog: Dialog
    ) {
        val map = HashMap<String, String>()
        map["product_id"] = product.id.toString()
        map["message"] = message
        viewModel.sendProductReport(map).observe(this, Observer { result ->
            dialog.sendReportProgress.visibility = View.GONE
            dialog.sendReportBut.visibility = View.VISIBLE

            if (result != null) {
                if (result.result == true) {
                    dialog.dismiss()
                    makeToast(this, result.errorMesage.toString())
                } else
                    makeToast(this, result.errorMesage.toString())
            } else
                makeToast(this, resources.getString(R.string.error))
        })
    }

    private fun showDialog(product: ProductData) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.report_dialog)

        dialog.sendReportBut.setOnClickListener {
            dialog.sendReportProgress.visibility = View.VISIBLE
            dialog.sendReportBut.visibility = View.GONE
            val message = dialog.sendReportMessage.text.toString()
            sendProductReport(product, message, dialog)
        }
        dialog.show()

    }

    private fun addProductToFav(product: ProductData) {
        if (product.isFavorite == null) {
            viewModel.addToFav(product.id!!).observe(this, Observer { result ->
                if (result != null) {
                    if (result.result == true) {
                        makeToast(this, resources.getString(R.string.added))
                        productFav.setImageResource(R.mipmap.fav_hov_1mdpi)
                        product.isFavorite = result.insertID
                    } else
                        makeToast(this, result.errorMesage.toString())
                } else
                    makeToast(this, resources.getString(R.string.error))
            })
        } else {
            viewModel.removeFromFav(product.isFavorite!!).observe(this, Observer { result ->
                if (result != null) {
                    if (result.result == true) {
                        productFav.setImageResource(R.mipmap.favmdpi)
                        product.isFavorite = null
                    } else
                        makeToast(this, result.errorMesage.toString())
                } else
                    makeToast(this, resources.getString(R.string.error))
            })
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (intent?.extras!!.getBoolean("fromNotification")) {
            val intent = Intent(this, MainCategoriesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPreferenceUtil(newBase!!).getString(LANG, "ar")
        super.attachBaseContext(ApplicationLanguageHelper.wrap(newBase!!, "$lang"))
    }
}
