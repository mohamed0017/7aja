package com.haja.haja.View.ui.SplashScreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.haja.haja.R
import com.haja.haja.Utils.ApplicationLanguageHelper
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.USERID
import com.haja.haja.View.ui.AdScreen.AdActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /** Duration of wait  */
        val length = 2000
        /* New Handler to start the Main Activity
         * and close this Splash-Screen after 2 seconds.*/

        Handler().postDelayed({
            // TODO remove this later
            SharedPreferenceUtil(this).putString(USERID , "167")
            startActivity(Intent(this, AdActivity::class.java))
            finish()
        }, length.toLong())
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ApplicationLanguageHelper.wrap(newBase!!, "ar"))
    }
}
