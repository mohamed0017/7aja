package com.infovass.lawyerskw.lawyerskw.Utils.ui

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class SnackAndToastUtil {
    companion object {
        fun makeSnackbar(view: View, message: String) =
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()

        fun makeToast(ctx: Context, message: String) =
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
    }
}