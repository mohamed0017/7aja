package com.haja.hja

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.util.Log
import androidx.multidex.MultiDex
import com.haja.hja.Utils.LocalizationHelper
import com.haja.hja.View.ui.SplashScreen.SplashActivity
import com.onesignal.OneSignal
import java.util.*

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // OneSignal Initialization
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .setNotificationReceivedHandler {
                val data = it.payload.additionalData
                Log.i("notification data ", data.toString())
            }
            .setNotificationOpenedHandler {
                val data = it.notification.payload.additionalData
                if (data.getInt("type") == 1) {
                    val productId = data.getString("id")
                    val intent = Intent(applicationContext, SplashActivity::class.java)
                    intent.putExtra("product_Id", productId)
                    intent.putExtra("from_Notification", true)
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, SplashActivity::class.java)
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
            .init()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocalizationHelper.updateBaseContextLocale(base))
        MultiDex.install(this)
    }

}