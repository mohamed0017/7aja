package com.haja.haja.View.ui.ProductDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.haja.haja.R
import com.haja.haja.Service.ApiService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image_full_screen.*

class ImageFullScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_full_screen)

        fullScreenClose.setOnClickListener {
            finish()
        }
        Picasso.get().load(ApiService.IMAGEBASEURL +intent.getStringExtra("fullScreenImg"))
            .placeholder(getResources().getDrawable(R.drawable.placeholder))
            .error(getResources().getDrawable(R.drawable.placeholder))
            .into(fullScreenImg)
    }
}
