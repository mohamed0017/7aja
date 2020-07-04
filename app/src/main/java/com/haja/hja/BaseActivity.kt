package com.haja.hja

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.haja.hja.Utils.LocalizationHelper


open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocalizationHelper.updateBaseContextLocale(this)
        }
        super.onCreate(savedInstanceState)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocalizationHelper.updateBaseContextLocale(newBase))

    }

}