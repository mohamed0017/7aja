package com.haja.haja.Utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.haja.haja.R
import kotlinx.android.synthetic.main.dialog_message.*

class CustomDialog {

    companion object {
        fun showDialog(context: Context, msg: String) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_message)
            dialog.dialogMessage.text = msg
            dialog.done.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }


}