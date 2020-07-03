package com.haja.hja.View.ui.ProductDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.haja.hja.R
import com.haja.hja.Service.model.ProductImgs
import com.haja.hja.View.Adapter.SliderAdapterExample
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import kotlinx.android.synthetic.main.activity_image_full_screen.*

class ImageFullScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_image_full_screen)

        back.setOnClickListener {
            finish()
        }

        val imgs = intent.getParcelableArrayListExtra<ProductImgs>("fullScreenImg")
        if (!imgs.isNullOrEmpty())
            imageSlider.sliderAdapter = SliderAdapterExample(this, imgs)
        // imageSlider.startAutoCycle()
        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM)
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)

    }
}
