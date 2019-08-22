package com.haja.haja.View.ui.ProductDetails

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.haja.haja.R
import com.haja.haja.Service.model.ProductData
import com.haja.haja.View.Adapter.AttributsAdapter
import com.haja.haja.View.Adapter.SliderAdapterExample
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import kotlinx.android.synthetic.main.product_details_fragment.*

class ProductDetailsFragment : Fragment() {

    companion object {
        fun newInstance(product: ProductData?) = ProductDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable("productData" , product)
            }
        }
    }

    private lateinit var viewModel: ProductDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val product = arguments?.getParcelable("productData") as ProductData
        viewModel = ViewModelProviders.of(this).get(ProductDetailsViewModel::class.java)

        productName.text = product.name
        productCat.text = product.catName
        productViews.text = product.numViews.toString()
        productDescription.text = product.description

        productAttributes.layoutManager = LinearLayoutManager(context)
        productAttributes.isNestedScrollingEnabled = false
        if (!product.attributes.isNullOrEmpty())
        productAttributes.adapter = AttributsAdapter(product.attributes)

        if (!product.imgs.isNullOrEmpty())
        imageSlider.sliderAdapter =  SliderAdapterExample(context, product.imgs)

       // imageSlider.startAutoCycle()
        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM)
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)


    }

}
