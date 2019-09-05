package com.haja.haja.View.ui.SplashScreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.haja.haja.R
import com.haja.haja.Utils.ApplicationLanguageHelper
import com.haja.haja.Utils.LANG
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.USERID
import com.haja.haja.View.ui.AdScreen.AdActivity
import com.haja.haja.View.ui.ProductDetails.ProductDetailsActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /** Duration of wait  */
        val length = 2000
        /* New Handler to start the Main Activity
         * and close this Splash-Screen after 2 seconds.*/

        Handler().postDelayed({
            val productId = intent?.extras?.getInt("productId")
            if (intent?.extras?.getInt("productId") != null && intent?.extras?.getBoolean("fromNotification") == true) {
                val intent = Intent(applicationContext, ProductDetailsActivity::class.java)
                intent.putExtra("productId", productId)
                intent.putExtra("fromNotification", true)
                startActivity(intent)
                finish()
            } else {
                startActivity(Intent(this, AdActivity::class.java))
                finish()
            }

        }, length.toLong())
    }

    override fun attachBaseContext( newBase: Context?) {
        val lang = SharedPreferenceUtil(newBase!!).getString(LANG, "ar")
        super.attachBaseContext(ApplicationLanguageHelper.wrap(newBase, lang.toString()))
    }
}
