package com.haja.hja.View.ui.SplashScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.haja.hja.R
import com.haja.hja.Utils.ApplicationLanguageHelper
import com.haja.hja.Utils.LANG
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.View.ui.AdScreen.AdActivity
import com.haja.hja.View.ui.ProductDetails.ProductDetailsActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /** Duration of wait  */
        val length = 2000
        /* New Handler to start the Main Activity
         * and close this Splash-Screen after 2 seconds.*/
        Handler().postDelayed({
            val productId = intent.getStringExtra("product_Id")
            if (intent.getBooleanExtra("from_Notification", false)) {
                val intent = Intent(applicationContext, ProductDetailsActivity::class.java)
                intent.putExtra("productId", productId?.toInt())
                intent.putExtra("fromNotification", true)
                startActivity(intent)
                finish()
            } else {
                startActivity(Intent(this, AdActivity::class.java))
                finish()
            }

        }, length.toLong())
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPreferenceUtil(newBase!!).getString(LANG, "ar")
        super.attachBaseContext(ApplicationLanguageHelper.wrap(newBase, lang.toString()))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
        Log.e("onNewIntent" , intent?.extras.toString() + "....")
    }

    override fun onResume() {
        super.onResume()
        Log.e("onResume", "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("onRestart", "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.e("onStart", "onStart")

    }
}
