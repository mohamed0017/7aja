package com.haja.haja.View.ui.AdScreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.haja.haja.R
import com.haja.haja.Service.ApiService
import com.haja.haja.Service.model.AdsModel
import com.haja.haja.Utils.ApplicationLanguageHelper
import com.haja.haja.Utils.LANG
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.View.ui.MainCategoriesActivity.MainCategoriesActivity
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_ad.*

class AdActivity : AppCompatActivity() {

    lateinit var viewModel: StartupAdViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)
        val offerName = intent.getStringExtra("offerName")
        val numViews = intent.getStringExtra("numViews")
        val offerImg = intent.getStringExtra("offerImage")
        viewModel = ViewModelProviders.of(this).get(StartupAdViewModel::class.java)
        if (offerName != null) {
            startupAdName.text = offerName
            AdViews.text = numViews
            Picasso.get().load(ApiService.IMAGEBASEURL + offerImg)
                .placeholder(resources.getDrawable(R.drawable.placeholder))
                .error(resources.getDrawable(R.drawable.placeholder)).into(startupAdImg)
            closeAd.setOnClickListener {
                onBackPressed()
            }
        } else {
            getStartupAd()
            closeAd.setOnClickListener {
                startActivity(Intent(this, MainCategoriesActivity::class.java))
                finish()
            }
        }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
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
        startupAdName.text = ad.data?.get(0)?.name.toString()
        Picasso.get().load(ApiService.IMAGEBASEURL + "${ad.data?.get(0)?.img}")
            .placeholder(resources.getDrawable(R.drawable.placeholder))
            .error(resources.getDrawable(R.drawable.placeholder)).into(startupAdImg)
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPreferenceUtil(newBase!!).getString(LANG, "ar")
        super.attachBaseContext(ApplicationLanguageHelper.wrap(newBase, "$lang"))
    }
}
