package com.haja.haja.View.ui.EditProduct

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
import com.haja.haja.OnCategoryItemClick
import com.haja.haja.R
import com.haja.haja.Service.model.AdPricedataModel
import com.haja.haja.Service.model.AttributeData
import com.haja.haja.Service.model.ProductImgs
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.USERID
import com.haja.haja.Utils.inTransaction
import com.haja.haja.View.ui.AddProduct.*
import com.haja.haja.View.ui.MyAdsScreen.MyAdsFragment
import com.haja.haja.model.CategoriesData
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_edit_product.*
import kotlinx.android.synthetic.main.activity_edit_product.proPhone
import kotlinx.android.synthetic.main.activity_edit_product.proWhatsApp
import kotlinx.android.synthetic.main.add_products_categories.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_message.*
import kotlinx.android.synthetic.main.dialog_message_price.*
import kotlinx.android.synthetic.main.dialog_message_price.done
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.HashMap

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
    private var adPricedata: AdPricedataModel? = null

    private val adapter = EditProductImagesAdapter(this)
    var productId = 0

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
            uploadProduct()
        }
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
                    if (!product.data.imgs.isNullOrEmpty())
                        adapter.setImagesAndNotifyList(product.data.imgs as ArrayList<ProductImgs?>?)
                } else
                    makeToast(context!!, resources.getString(R.string.error))
            })
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


/*    private fun advStared() {
        editAdvStared.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                showStaredDialog()
            }else{

            }
        }
    }*/

    private fun showStaredDialog() {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_message)
        dialog.done.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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
                    }
            }
        })
    }

    private fun uploadProduct() {
        val progress = CustomProgressBar.showProgressBar(context!!)
        progress.show()
        viewModel.setProductAttributes(attributesAdapter.getEnteredAttributesData())
        viewModel.setImages(getSelectedImages())
        viewModel.setProductData(getProductData())
        viewModel.editProduct(productId).observe(this, Observer { products ->
            progress.dismiss()
            if (products != null) {
                makeToast(context!!, products.errorMesage.toString())
                fragmentManager?.inTransaction {
                    replace(R.id.mainContainer, MyAdsFragment.newInstance())
                }
            } else {
                makeToast(context!!, resources.getString(R.string.error))
            }
        })
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
            dialog.adsFreeCount.text = advPrice
            dialog.adsFreeMsg.text = resources.getString(R.string.no_free_ads)
        }else
            dialog.adsFreeCount.text = adPricedata?.userFreeAds.toString()

        dialog.done.setOnClickListener {
         //   if (adPricedata?.userFreeAds!! > 0) {
                uploadProduct()
           /* } else {
                val intent = Intent(context!!, PaymentActivity::class.java)
                intent.putExtra(PaymentActivity.PAYMENT_AMOUNT, advPrice)
                startActivityForResult(intent, PaymentActivity.PAYMENT_REQUEST_CODE)
            }*/
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
        map["is_special"] = "0"
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

        super.onActivityResult(requestCode, resultCode, data)  // You MUST have context!! line to be here
        // so ImagePicker can work with fragment mode
    }

    private fun openGallery() {
        ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
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
            //  .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
            .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
            .setDoneTitle("Done")               //  Done button title
            .setLimitMessage("You have reached selection limit")    // Selection limit message
            .setMaxSize(6)                     //  Max images can be selected
            // .setSavePath("ImagePicker")         //  Image capture folder name
            .setSelectedImages(selectedImages)            //  Selected images
            .setRequestCode(100)                //  Set request code, default Config.RC_PICK_IMAGES
            .setKeepScreenOn(true)              //  Keep screen on when selecting images
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