package com.haja.haja.View.ui.AddProduct

import android.content.Context
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.haja.haja.R

class AddProductCategoriesDialog {

    lateinit var dialog: MaterialDialog

    fun show(context: Context) {
         dialog = MaterialDialog(context).show {
            customView(R.layout.add_products_categories)
             // literal, internally converts to dp so 16dp
             cornerRadius(16f)
        }

    }

    fun getDialogView(): View {
       return dialog.getCustomView()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}