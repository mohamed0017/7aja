package com.infovass.lawyerskw.lawyerskw.Utils.ui

import android.content.Context
import com.haja.hja.R
import com.kaopiz.kprogresshud.KProgressHUD

class CustomProgressBar {
    companion object {
        fun showProgressBar(context: Context): KProgressHUD {

            return KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(context.resources.getString(R.string.wait))
                    .setCancellable(true)
                    .setWindowColor(context.resources.getColor(R.color.colorPrimary))
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
        }

        fun showProgressBar(context: Context, message: String): KProgressHUD {
            return KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(message)
                    .setCancellable(true)
                    .setWindowColor(context.resources.getColor(R.color.colorPrimary))
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
        }
    }
}