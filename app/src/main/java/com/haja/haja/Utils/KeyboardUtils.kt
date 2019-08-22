package com.infovass.lawyerskw.lawyerskw.Utils.ui

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

class KeyboardUtils {

    companion object {
        fun hideKeyboard(activity: Activity) {
            // Check if no view has focus:
            val view = activity.currentFocus

            if (view != null) {
                val imm = activity.getSystemService(
                        Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        fun showKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }
}