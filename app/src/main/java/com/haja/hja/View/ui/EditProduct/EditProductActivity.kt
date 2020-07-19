package com.haja.hja.View.ui.EditProduct

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haja.hja.OnCategoryItemClick
import com.haja.hja.R
import com.haja.hja.Service.model.AdPricedataModel
import com.haja.hja.Service.model.AttributeData
import com.haja.hja.Service.model.ProductImgs
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.USERID
import com.haja.hja.Utils.inTransaction
import com.haja.hja.View.ui.AddProduct.AddProductAttributesAdapter
import com.haja.hja.View.ui.AddProduct.AddProductCatAdapter
import com.haja.hja.View.ui.AddProduct.AddProductCategoriesDialog
import com.haja.hja.View.ui.AddProduct.SelectedImagesAdapter
import com.haja.hja.View.ui.MyAdsScreen.MyAdsFragment
import com.haja.hja.View.ui.Payment.PaymentActivity
import com.haja.hja.model.CategoriesData
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_edit_product.*
import kotlinx.android.synthetic.main.activity_edit_product.lettersNumber
import kotlinx.android.synthetic.main.activity_edit_product.proPhone
import kotlinx.android.synthetic.main.activity_edit_product.proWhatsApp
import kotlinx.android.synthetic.main.activity_edit_product.selectCategoryToShowAtt
import kotlinx.android.synthetic.main.add_products_categories.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_message.*
import kotlinx.android.synthetic.main.dialog_message_price.*
import kotlinx.android.synthetic.main.dialog_message_price.done
import kotlinx.android.synthetic.main.dialog_stared.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class EditProductActivity : Fragment(), OnDeleteImage, OnCategoryItemClick {

    companion object {
        fun newInstance(id: Int) = EditProductActivity().apply {
            arguments = Bundle().apply {
                putInt("productId", id)
            }
        }
    }

    private lateinit var viewModel: EditProductViewModel
    private lateinit var categoriesAdapter: AddProductCatAdapter
    private var attributesAdapter = AddProductAttributesAdapter()
    private val categoriesDialog = AddProductCategoriesDialog()
    private var selectedImages = ArrayList<Image>()
    private var selectedCategory = 0
    private var selectedCategoriesCount = 0
    private var advPrice = "0"
    private var isPublished = "N"
    private var adPricedata: AdPricedataModel? = null
    private val adapter = EditProductImagesAdapter(this)
    var productId = 0
    private var totalAdPrice = "0"
    private var special12h = "0"
    private var special1day = "0"
    private var special2day = "0"
    private var seleectedSpecialTime = "0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_edit_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productId = arguments?.getInt("productId")!!
        viewModel = ViewModelProviders.of(this).get(EditProductViewModel::class.java)
        addNewProImagesList.layoutManager = GridLayoutManager(context!!, 4)
        editProImagesList.layoutManager = GridLayoutManager(context!!, 4)
        activity?.appBarTitle?.text = resources.getString(R.string.edit_ad)

        editUploadImages.setOnClickListener {
            openGallery()
        }

        editProductBut.setOnClickListener {
            if (isPublished == "N")
                uploadProduct()
            else {
                if (seleectedSpecialTime != "0") {
                    val intent = Intent(context!!, PaymentActivity::class.java)
                    intent.putExtra(PaymentActivity.PAYMENT_AMOUNT, totalAdPrice)
                    startActivityForResult(intent, PaymentActivity.PAYMENT_REQUEST_CODE)
                } else
                    showPriceDialog()
            }
        }
        getAdPrice()
        initDescriptionLettersCount()
        val userId = SharedPreferenceUtil(context!!).getString(USERID, "0")
        viewModel.getProduct(productId, userId!!.toInt())
            .observe(this, Observer { product ->
                if (product != null) {
                    editSelectCategory.text = product.data?.catName
                    editProNameAr.setText(product.data?.name)
                    editProPrice.setText(product.data?.price)
                    proPhone.setText(product.data?.mobile)
                    proWhatsApp.setText(product.data?.whatsapp)
                    editProDescriptionAr.setText(product.data?.description)
                    selectedCategory = product.data?.catId!!
                    setupAttributesList(selectedCategory)
                    addNewProImagesList.adapter = adapter
                    if (product.data.isSpecial != "0")
                        AdvStared.isChecked = true
                    isPublished = product.data.isPublished.toString()
                    if (product.data.isPublished == "Y")
                        AdvPublish.isChecked = true
                    if (!product.data.imgs.isNullOrEmpty())
                        adapter.setImagesAndNotifyList(product.data.imgs as ArrayList<ProductImgs?>?)
                } else
                    makeToast(context!!, resources.getString(R.string.error))
            })

        AdvPublish.setOnCheckedChangeListener { buttonView, isChecked ->
            isPublished = if (isChecked)
                "Y"
            else
                "N"
        }
        advStared()
    }

    override fun onDeleteImage(img: ProductImgs?) {
        viewModel.deleteImage(productId, img?.idImg!!).observe(this, Observer { result ->
            if (result != null) {
                if (result.result == true)
                    adapter.removeImageAndNotifyList(img)
                makeToast(context!!, result.errorMesage.toString())
            } else
                makeToast(context!!, resources.getString(R.string.error))
        })
    }

    override fun onClick(possion: Int, itemData: CategoriesData) {
        selectedCategoriesCount++
        if (selectedCategoriesCount == 2) {
            setupAttributesList(itemData.id)
        }
        if (itemData.countSubCat == 0) {
            selectedCategory = itemData.id!!
            editSelectCategory.text = itemData.name
            categoriesDialog.dismiss()
            categoriesAdapter.clearCategoriesList()
            categoriesAdapter.notifyDataSetChanged()
            selectedCategoriesCount = 0
        } else
            getNextCategories(itemData.id!!)
    }

    private fun initDescriptionLettersCount() {
        editProDescriptionAr.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lettersNumber.text = "600/${s?.length} ${resources.getString(R.string.letter)}"
                if (s?.length == 6000) {
                    lettersNumber.setTextColor(resources.getColor(R.color.red))
                }
            }

        })
    }

    private fun getAdPrice() {
        viewModel.getAdPrice().observe(this, Observer { adPrice ->
            if (adPrice != null) {
                if (adPrice.result == true)
                    if (adPrice.adPricedata != null) {
                        adPricedata = adPrice.adPricedata
                        advPrice = adPrice.adPricedata.priceAdv.toString()
                        totalAdPrice = advPrice
                        special12h = adPrice.adPricedata.special_4_12h.toString()
                        special1day = adPrice.adPricedata.special_4_1day.toString()
                        special2day = adPrice.adPricedata.special_4_2day.toString()
                    }
            }
        })
    }

    private fun uploadProduct() {
        val progress = CustomProgressBar.showProgressBar(context!!)
        progress.show()
        viewModel.setProductAttributes(attributesAdapter.getEnteredAttributesData())
        if (!selectedImages.isNullOrEmpty())
            viewModel.setImages(getSelectedImages())
        viewModel.setProductData(getProductData())
        viewModel.editProduct(productId).observe(this, Observer { products ->
            progress.dismiss()
            if (products != null) {
                showAddProductDoneDialog(products.errorMesage.toString())
            } else {
                makeToast(context!!, resources.getString(R.string.error))
            }
        })
    }

    private fun showAddProductDoneDialog(message: String) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_message)
        dialog.dialogMessage.text = message
        dialog.done.setOnClickListener {
            dialog.dismiss()
            fragmentManager?.inTransaction {
                replace(R.id.mainContainer, MyAdsFragment.newInstance())
            }
        }
        dialog.show()
    }

    private fun showDialog() {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_message)
        dialog.done.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDialog(message: String) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_message)
        dialog.dialogMessage.text = message
        dialog.done.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showPriceDialog() {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_message_price)
        if (adPricedata?.userFreeAds!! <= 0) {
            dialog.adsFreeCount.text = "$totalAdPrice ${resources.getString(R.string.kwd)}"
            dialog.adsFreeMsg.text = resources.getString(R.string.no_free_ads)
        } else
            dialog.adsFreeCount.text = adPricedata?.userFreeAds.toString()

        dialog.done.setOnClickListener {
            if (adPricedata?.userFreeAds!! > 0) {
                uploadProduct()
            } else {
                val intent = Intent(context!!, PaymentActivity::class.java)
                intent.putExtra(PaymentActivity.PAYMENT_AMOUNT, totalAdPrice)
                startActivityForResult(intent, PaymentActivity.PAYMENT_REQUEST_CODE)
            }
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun advStared() {
        AdvStared.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                showStaredDialog()
            } else {
                seleectedSpecialTime = "0"
                totalAdPrice = advPrice
            }
        }
    }

    private fun showStaredDialog() {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_stared)
        totalAdPrice = special12h
        dialog.hours.setOnClickListener {
            totalAdPrice = special12h
            seleectedSpecialTime = "12"
            dialog.dismiss()
        }
        dialog.oneDay.setOnClickListener {
            totalAdPrice = special1day
            seleectedSpecialTime = "24"
            dialog.dismiss()
        }
        dialog.twoDays.setOnClickListener {
            totalAdPrice = special2day
            seleectedSpecialTime = "48"
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getProductData(): HashMap<String, String> {
        val map = HashMap<String, String>()
        map["name"] = editProNameAr.text.toString()
        //   map["name_en"] = addProNameEn.text.toString()
        map["price"] = editProPrice.text.toString()
        //  map["discount"] = addProDiscount.text.toString()
        // map["quantity"] = addProQuantity.text.toString()
        map["description"] = editProDescriptionAr.text.toString()
        // map["description_en"] = addProDescriptionEn.text.toString()
        //    map["tags"] = addProTagsAr.text.toString()
        //  map["tags_en"] = addProTags.text.toString()
        map["cat_id"] = selectedCategory.toString()
        map["mobile"] = proPhone.text.toString()
        map["whatsapp"] = proWhatsApp.text.toString()
        /*      map["latitude"] = lati.toString()
              map["longitude"] = longi.toString()*/
        map["type"] = "1"
        map["is_special"] = seleectedSpecialTime
        map["is_published"] = isPublished
        map["user_id"] = SharedPreferenceUtil(context!!).getString(USERID, "0").toString()
        return map
    }

    private fun isValidProductData(): Boolean {
        return when {
            selectedImages.isNullOrEmpty() -> {
                makeToast(context!!, resources.getString(R.string.select_img))
                false
            }
            selectedCategory == 0 -> {
                makeToast(context!!, resources.getString(R.string.select_cat))
                false
            }
            else -> true
        }
    }

    private fun getSelectedImages(): List<MultipartBody.Part> {
        val images = ArrayList<MultipartBody.Part>()
        for (index in selectedImages.indices) {
            val file = File(selectedImages[index].path)
            val imageBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            images.add(MultipartBody.Part.createFormData("img[]", file.name, imageBody))
        }
        return images
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)
            // do your logic here...
            selectedImages = images
            val adapter = SelectedImagesAdapter(context!!)
            Log.i("onActivityResult", images[0].path + "...")
            editProImagesList.layoutManager = GridLayoutManager(context!!, 4)
            editProImagesList.adapter = adapter
            adapter.setImages(images)
            adapter.notifyDataSetChanged()

        }
        /* when (requestCode) {
             *//* EasyWayLocation.LOCATION_SETTING_REQUEST_CODE -> easyWayLocation?.onActivityResult(
                 resultCode
             )*//*
            PaymentActivity.PAYMENT_REQUEST_CODE -> if (data != null) {
                if (data.getBooleanExtra("payment_succeed", false)) uploadProduct()
                else showDialog(resources.getString(R.string.payment_failed_msg))
            }
        }*/

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )  // You MUST have context!! line to be here
        // so ImagePicker can work with fragment mode
    }

    private fun openGallery() {
        ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
            .setRootDirectoryName(Config.ROOT_DIR_DCIM)
            .setToolbarColor("#00aeef")         //  Toolbar color
            .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
            .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
            .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
            .setProgressBarColor("#4CAF50")     //  ProgressBar color
            .setBackgroundColor("#212121")      //  Background color
            .setCameraOnly(false)               //  Camera mode
            .setMultipleMode(true)              //  Select multiple images or single image
            .setFolderMode(false)                //  Folder mode
            .setShowCamera(true)                //  Show camera button
            .setShowNumberIndicator(true)
            //  .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
            .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
            .setDoneTitle("Done")               //  Done button title
            .setLimitMessage("You have reached selection limit")    // Selection limit message
            .setMaxSize(6)                     //  Max images can be selected
            // .setSavePath("ImagePicker")         //  Image capture folder name
            .setSelectedImages(selectedImages)            //  Selected images
            .setRequestCode(100)                //  Set request code, default Config.RC_PICK_IMAGES
            .start()
    }

    private fun getNextCategories(id: Int) {
        viewModel.setParentId(id)
        viewModel.getCategories().observe(this, Observer { categories ->
            if (categories != null) {
                categoriesAdapter.setCategoriesList(categories.data as ArrayList<CategoriesData>)
                runLayoutAnimation(categoriesDialog.getDialogView().addProductCategoriesList)
            }
        })
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right)
        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    private fun setupAttributesList(catId: Int?) {
        editProductAttributes.layoutManager = LinearLayoutManager(context!!)
        viewModel.getCategoryAttributes(catId).observe(this, Observer { attributes ->
            if (attributes != null) {
                selectCategoryToShowAtt.visibility = View.GONE
                if (attributes.result == true) {
                    editProductAttributes.adapter = attributesAdapter
                    attributesAdapter.setAttributes(attributes.data as ArrayList<AttributeData>)
                    if (attributes.data.isEmpty()) {
                        selectCategoryToShowAtt.visibility = View.VISIBLE
                        selectCategoryToShowAtt.text = resources.getString(R.string.no_att)
                    }
                } else {
                    attributesAdapter.clearAttributes()
                    attributesAdapter.notifyDataSetChanged()
                    makeToast(context!!, attributes.errorMesage.toString())
                }

            } else
                makeToast(context!!, resources.getString(R.string.error))
        })
    }

}