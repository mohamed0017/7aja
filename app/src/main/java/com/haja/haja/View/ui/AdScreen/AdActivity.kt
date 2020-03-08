package com.haja.haja.View.ui.AdScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.haja.haja.R
import com.haja.haja.Service.ApiService
import com.haja.haja.Service.model.AdsModel
import com.haja.haja.Utils.ApplicationLanguageHelper
import com.haja.haja.Utils.LANG
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.USERID
import com.haja.haja.View.ui.MainCategoriesActivity.MainCategoriesActivity
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_ad.*

class AdActivity : AppCompatActivity() {

    lateinit var viewModel: StartupAdViewModel
    private var isLike: String? = null
    private var likesCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)
        val offerId = intent.getStringExtra("offerId")
        val offerName = intent.getStringExtra("offerName")
        val numViews = intent.getStringExtra("numViews")
        val likesCount = intent.getStringExtra("offerLikes")
        val offerImg = intent.getStringExtra("offerImage")
        val isLike = intent.getStringExtra("isLike")
        val mobile = intent.getStringExtra("mobile")
        val isFromOffersScreen = intent.getBooleanExtra("fromOffersScreen", false)

        viewModel = ViewModelProviders.of(this).get(StartupAdViewModel::class.java)

        if (offerName != null) {
            this.isLike = isLike
            this.likesCount = likesCount.toInt()
            if (!mobile.isNullOrEmpty()) {
                advMobile.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mobile, null))
                    startActivity(intent)
                }
            }
            if (!isLike.isNullOrEmpty()) {
                advLike.setImageResource(R.mipmap.like_hovmdpi)
            }
            if (numViews != null)
                AdViews.text = (numViews.toInt() + 1).toString()

            startupAdLikes.text = likesCount
            startupAdName.text = offerName
            Picasso.get().load(ApiService.IMAGEBASEURL + offerImg)
                .placeholder(resources.getDrawable(R.drawable.placeholder))
                .error(resources.getDrawable(R.drawable.placeholder)).into(startupAdImg)

            closeAd.setOnClickListener {
                onBackPressed()
            }
            incrementViews(isFromOffersScreen, offerId)
        } else {

            getStartupAd()
            closeAd.setOnClickListener {
                startActivity(Intent(this, MainCategoriesActivity::class.java))
                finish()
            }
        }
    }

    private fun incrementViews(
        fromOffersScreen: Boolean,
        offerId: String
    ) {
        if (fromOffersScreen) {
            advLike.setOnClickListener {
                val userId = SharedPreferenceUtil(this).getString(USERID, "0")
                if (userId == "0")
                    makeToast(this, resources.getString(R.string.login_first))
                else
                    likeAdv(0, offerId)
            }
            viewModel.incrementOfferViews(offerId.toInt()).observe(this, Observer {

            })
        } else {
            advLike.setOnClickListener {
                val userId = SharedPreferenceUtil(this).getString(USERID, "0")
                if (userId == "0")
                    makeToast(this, resources.getString(R.string.login_first))
                else
                    likeAdv(1, offerId)

            }

            viewModel.incrementAdvertisementViews(offerId.toInt()).observe(this, Observer {

            })
        }
    }

    private fun likeAdv(type: Int, offerId: String) {
        viewModel.likeAdvertisement(type, offerId.toInt()).observe(this, Observer { result ->
            if (result != null) {
                if (result.result == true) {
                    if (isLike == null) {
                        startupAdLikes.text = (++likesCount).toString()
                        advLike.setImageResource(R.mipmap.like_hovmdpi)
                        isLike = "0"
                    } else {
                        startupAdLikes.text = (--likesCount).toString()
                        advLike.setImageResource(R.mipmap.like)
                        isLike = null
                    }

                } else
                    makeToast(this, result.errorMesage.toString())
            } else
                makeToast(this, resources.getString(R.string.error))

        })
    }

    private fun getStartupAd() {
        viewModel.getStartupAd().observe(this, Observer { ad ->
            if (ad != null) {
                if (ad.result == true)
                    setUpAdData(ad)
                else
                    Toast.makeText(this, ad.errorMessage, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, resources.getString(R.string.error), Toast.LENGTH_SHORT).show()
        })
    }

    private fun setUpAdData(ad: AdsModel) {
        if (!ad.data.isNullOrEmpty()) {
            isLike = ad.data[0]?.isLike
            likesCount = ad.data[0]?.likes!!.toInt()
            if (ad.data[0]?.isLike != null)
                advLike.setImageResource(R.mipmap.like_hovmdpi)

            startupAdLikes.text = ad.data[0]?.likes
            val views = ad.data[0]?.numViews
            if (views != null)
                AdViews.text = (views.toInt() + 1).toString()
            startupAdName.text = ad.data[0]?.name.toString()
            Picasso.get().load(ApiService.IMAGEBASEURL + "${ad.data.get(0)?.img}")
                .placeholder(resources.getDrawable(R.drawable.placeholder))
                .error(resources.getDrawable(R.drawable.placeholder)).into(startupAdImg)

            if (!ad.data[0]?.mobile.isNullOrEmpty()) {
                advMobile.setOnClickListener {
                    val intent =
                        Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", ad.data[0]?.mobile, null))
                    startActivity(intent)
                }
            }

            advLike.setOnClickListener {
                val userId = SharedPreferenceUtil(this).getString(USERID, "0")
                if (userId == "0")
                    makeToast(this, resources.getString(R.string.login_first))
                else {
                    viewModel.likeAdvertisement(1, ad.data[0]?.id!!)
                        .observe(this, Observer { result ->
                            if (result != null) {
                                if (result.result == true) {
                                    if (ad.data[0]?.likes != null) {
                                        if (isLike == null) {
                                            startupAdLikes.text = (++likesCount).toString()
                                            advLike.setImageResource(R.mipmap.like_hovmdpi)
                                            isLike = "0"
                                        } else {
                                            startupAdLikes.text = (--likesCount).toString()
                                            advLike.setImageResource(R.mipmap.like)
                                            isLike = null
                                        }
                                    }
                                } else
                                    makeToast(this, result.errorMesage.toString())
                            } else
                                makeToast(this, resources.getString(R.string.error))

                        })
                }
            }
            viewModel.incrementAdvertisementViews(ad.data[0]?.id!!).observe(this, Observer {
            })
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPreferenceUtil(newBase!!).getString(LANG, "ar")
        super.attachBaseContext(ApplicationLanguageHelper.wrap(newBase, "$lang"))
    }
}
