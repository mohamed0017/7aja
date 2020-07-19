package com.haja.hja.View.ui.AddProduct

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Toast
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
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.USERID
import com.haja.hja.Utils.inTransaction
import com.haja.hja.View.ui.MyAdsScreen.MyAdsFragment
import com.haja.hja.View.ui.Payment.PaymentActivity
import com.haja.hja.model.CategoriesData
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.add_product_fragment.*
import kotlinx.android.synthetic.main.add_products_categories.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_message.*
import kotlinx.android.synthetic.main.dialog_message.done
import kotlinx.android.synthetic.main.dialog_message_price.*
import kotlinx.android.synthetic.main.dialog_stared.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AddProductFragment : Fragment(), OnCategoryItemClick {

    /* override fun locationCancelled() {
     }

     override fun locationOn() {
         *//*easyWayLocation?.beginUpdates()
        try {
            lati = easyWayLocation?.latitude
            longi = easyWayLocation?.longitude

            if (lati != null && longi != null)
                cityName.text = getAreaName()
        } catch (e: Exception) {
            e.printStackTrace()
        }*//*
    }

    override fun onPositionChanged() {
        easyWayLocation?.beginUpdates()
        try {
            lati = easyWayLocation?.latitude
            longi = easyWayLocation?.longitude

            if (lati != null && longi != null)
                cityName.text = getAreaName()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
*/

/*    private fun getAreaName(): String {
        val geocoder = Geocoder(context, Locale.getDefault());
        val addresses = geocoder.getFromLocation(lati!!, longi!!, 1);
        val cityName = addresses.get(0).getAddressLine(0);
        val stateName = addresses.get(0).getAddressLine(1);
        // val countryName = addresses.get(0).getAddressLine(2);

        return "$cityName  "
    }*/

    override fun onClick(possion: Int, itemData: CategoriesData) {
        selectedCategoriesCount++
        if (selectedCategoriesCount == 2) {
            setupAttributesList(itemData.id)
        }
        if (itemData.countSubCat == 0) {
            selectedCategory = itemData.id!!
            selectCategory.text = itemData.name
            categoriesDialog.dismiss()
            categoriesAdapter.clearCategoriesList()
            categoriesAdapter.notifyDataSetChanged()
            selectedCategoriesCount = 0
        } else
            getNextCategories(itemData.id!!)
    }

    companion object {
        fun newInstance() = AddProductFragment()
    }

    private lateinit var viewModel: AddProductViewModel
    private lateinit var categoriesAdapter: AddProductCatAdapter
    private var attributesAdapter = AddProductAttributesAdapter()
    /*    private var easyWayLocation: EasyWayLocation? = null
        private var lati: Double? = null
        private var longi: Double? = null*/
    private val categoriesDialog = AddProductCategoriesDialog()
    private var selectedImages = ArrayList<Image>()
    private var selectedCategory = 0
    private var selectedCategoriesCount = 0
    private var totalAdPrice = "0"
    private var advPrice = "0"
    private var seleectedSpecialTime = "0"
    private var special12h = "0"
    private var special1day = "0"
    private var special2day = "0"
    private var is_published = "Y"
    private var adPricedata: AdPricedataModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_product_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // showDialog()
        initDescriptionLettersCount()
        activity?.appBarTitle?.text = resources.getString(R.string.addProduct)
        activity?.categoriesBarBack?.visibility = View.GONE
        activity?.categoriesBarMenu?.visibility = View.VISIBLE
        activity?.catBarSearch?.visibility = View.GONE

        viewModel = ViewModelProviders.of(this).get(AddProductViewModel::class.java)
        categoriesAdapter = AddProductCatAdapter(this)
        // configLocationPremation()
        getAdPrice()
        uploadImages.setOnClickListener {
            openGallery()
        }

        notPublishProductBut.setOnClickListener {
            if (isValidProductData()) {
                is_published = "N"
                uploadProduct()
            }
        }
        selectCategory.setOnClickListener {
            selectedCategoriesCount = 0
            categoriesDialog.show(context!!)
            categoriesDialog.getDialogView().addProductCategoriesList.layoutManager =
                LinearLayoutManager(context!!)
            categoriesDialog.getDialogView().addProductCategoriesList.adapter = categoriesAdapter
            viewModel.setParentId(0)
            viewModel.getCategories().observe(this, Observer { categories ->
                if (categories != null) {
                    categoriesAdapter.setCategoriesList(categories.data as ArrayList<CategoriesData>)
                    runLayoutAnimation(categoriesDialog.getDialogView().addProductCategoriesList)
                } else {

                }
            })
        }

        addProductBut.setOnClickListener {
            if (isValidProductData()) {
                is_published = "Y"
                if (seleectedSpecialTime != "0"){
                    val intent = Intent(context!!, PaymentActivity::class.java)
                    intent.putExtra(PaymentActivity.PAYMENT_AMOUNT, totalAdPrice)
                    startActivityForResult(intent, PaymentActivity.PAYMENT_REQUEST_CODE)
                }else
                showPriceDialog()
            }
        }

        advStared()
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

    private fun initDescriptionLettersCount() {
        addProDescriptionAr.addTextChangedListener(object : TextWatcher {
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
        viewModel.setImages(getSelectedImages())
        viewModel.setProductData(getProductData())
        viewModel.addProduct().observe(this, Observer { products ->
            progress.dismiss()
            if (products != null && products.result == true) {
                showAddProductDoneDialog(products.errorMesage.toString())
            } else if (products != null && !products.errorMesage.isNullOrEmpty()){
                makeToast(context!!, products.errorMesage.toString())
            }else
                makeToast(context!!, resources.getString(R.string.error))

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

    private fun getProductData(): HashMap<String, String> {
        val map = HashMap<String, String>()
        map["name"] = addProNameAr.text.toString()
        map["name_en"] = addProNameEn.text.toString()
        map["price"] = addProPrice.text.toString()
        //  map["discount"] = addProDiscount.text.toString()
        // map["quantity"] = addProQuantity.text.toString()
        map["description"] = addProDescriptionAr.text.toString()
        map["description_en"] = addProDescriptionEn.text.toString()
        //    map["tags"] = addProTagsAr.text.toString()
        //  map["tags_en"] = addProTags.text.toString()
        map["cat_id"] = selectedCategory.toString()
        map["mobile"] = proPhone.text.toString()
        map["whatsapp"] = proWhatsApp.text.toString()
        /*      map["latitude"] = lati.toString()
              map["longitude"] = longi.toString()*/
        map["type"] = "1"
        map["is_special"] = seleectedSpecialTime
        map["is_published"] = is_published
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
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)
            // do your logic here...
            var imagesSize = 0
            images?.forEach {
                val file = File(it.path)
                val file_size = Integer.parseInt((file.length() / 1024).toString())
                imagesSize = imagesSize + file_size
            }
            if (imagesSize > 10000){
                Toast.makeText(context, getString(R.string.max_size_images), Toast.LENGTH_LONG).show()
                openGallery()
                return
            }
            selectedImages = images
            val adapter = SelectedImagesAdapter(context!!)
            //Log.i("onActivityResult", images[0].path + "...")
            selectedImagesList.layoutManager = GridLayoutManager(context!!, 4)
            selectedImagesList.adapter = adapter
            adapter.setImages(images)
            adapter.notifyDataSetChanged()

        }
        when (requestCode) {
            /* EasyWayLocation.LOCATION_SETTING_REQUEST_CODE -> easyWayLocation?.onActivityResult(
                 resultCode
             )*/
            PaymentActivity.PAYMENT_REQUEST_CODE -> if (data != null) {
                if (data.getBooleanExtra("payment_succeed", false)) uploadProduct()
                else showDialog(resources.getString(R.string.payment_failed_msg))
            }
        }

        super.onActivityResult(requestCode, resultCode, data)  // You MUST have this line to be here
        // so ImagePicker can work with fragment mode
    }

    private fun openGallery() {
        ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
            .setFolderMode(false)
            .setRootDirectoryName(Config.ROOT_DIR_DCIM)
            .setCameraOnly(false)               //  Camera mode
            .setMultipleMode(true)              //  Select multiple images or single image
            .setShowCamera(true)                //  Show camera button
            //  .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
            .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
            .setDoneTitle(getString(R.string.done))               //  Done button title
            .setLimitMessage(getString(R.string.upload_img))    // Selection limit message
            .setMaxSize(4)                     //  Max images can be selected
            .setShowNumberIndicator(true)
            // .setSavePath("ImagePicker")         //  Image capture folder name
            .setSelectedImages(selectedImages)            //  Selected images
            .setRequestCode(100)                //  Set request code, default Config.RC_PICK_IMAGES
            .setToolbarColor("#00aeef")         //  Toolbar color
            .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
            .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
            .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
            .setProgressBarColor("#4CAF50")     //  ProgressBar color
            .setBackgroundColor("#212121")      //  Background color
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
        addProductAttributes.layoutManager = LinearLayoutManager(context!!)
        viewModel.getCategoryAttributes(catId).observe(this, Observer { attributes ->
            if (attributes != null) {
                selectCategoryToShowAtt.visibility = View.GONE
                if (attributes.result == true) {
                    addProductAttributes.adapter = attributesAdapter
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

    /* private fun configLocationPremation() {
         Dexter.withActivity(activity)
             .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
             .withListener(object : PermissionListener {
                 override fun onPermissionRationaleShouldBeShown(
                     permission: com.karumi.dexter.listener.PermissionRequest?,
                     token: PermissionToken?
                 ) {
                     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                 }

                 @SuppressLint("MissingPermission")
                 override fun onPermissionGranted(response: PermissionGrantedResponse) {
                     easyWayLocation = EasyWayLocation(context!!)
                     easyWayLocation?.setListener(this@AddProductFragment)
                 }

                 override fun onPermissionDenied(response: PermissionDeniedResponse) {
                     Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                 }

             }).check()
     }

     override fun onResume() {
         super.onResume()
         // make the device update its location
         easyWayLocation?.beginUpdates()
     }

     override fun onPause() {
         // stop location updates (saves battery)
         easyWayLocation?.endUpdates()
         super.onPause()
     }*/
}
