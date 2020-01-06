package com.haja.haja.View.ui.AddProduct

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
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
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.Listener
import com.haja.haja.OnCategoryItemClick
import com.haja.haja.R
import com.haja.haja.Service.model.AttributeData
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.USERID
import com.haja.haja.Utils.inTransaction
import com.haja.haja.View.ui.MyAdsScreen.MyAdsFragment
import com.haja.haja.View.ui.Payment.PaymentActivity
import com.haja.haja.model.CategoriesData
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.add_product_fragment.*
import kotlinx.android.synthetic.main.add_products_categories.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_message.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AddProductFragment : Fragment(), OnCategoryItemClick, Listener {

    override fun locationCancelled() {
    }

    override fun locationOn() {
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

    private fun getAreaName(): String {
        val geocoder = Geocoder(context, Locale.getDefault());
        val addresses = geocoder.getFromLocation(lati!!, longi!!, 1);
        val cityName = addresses.get(0).getAddressLine(0);
        val stateName = addresses.get(0).getAddressLine(1);
        // val countryName = addresses.get(0).getAddressLine(2);

        return "$cityName  "
    }

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
    private var easyWayLocation: EasyWayLocation? = null
    private var lati: Double? = null
    private var longi: Double? = null
    private val categoriesDialog = AddProductCategoriesDialog()
    private var selectedImages = ArrayList<Image>()
    private var selectedCategory = 0
    private var selectedCategoriesCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_product_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       // showDialog()
        activity?.appBarTitle?.text = resources.getString(R.string.addProduct)
        activity?.categoriesBarBack?.visibility = View.GONE
        activity?.categoriesBarMenu?.visibility = View.VISIBLE
        activity?.catBarSearch?.visibility = View.GONE

        viewModel = ViewModelProviders.of(this).get(AddProductViewModel::class.java)
        categoriesAdapter = AddProductCatAdapter(this)
        configLocationPremation()
        val progress = CustomProgressBar.showProgressBar(context!!)

        uploadImages.setOnClickListener {
            openGallery()
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
                val intent = Intent(context!!, PaymentActivity::class.java)
                intent.putExtra(PaymentActivity.PAYMENT_AMOUNT, "500")
                startActivityForResult(intent, PaymentActivity.PAYMENT_REQUEST_CODE)
            }
        }
    }

    private fun uploadProduct() {
        val progress = CustomProgressBar.showProgressBar(context!!)
        progress.show()
        viewModel.setProductAttributes(attributesAdapter.getEnteredAttributesData())
        viewModel.setImages(getSelectedImages())
        viewModel.setProductData(getProductData())
        viewModel.addProduct().observe(this, Observer { products ->
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

    private fun showDialog(message : String) {
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
        map["latitude"] = lati.toString()
        map["longitude"] = longi.toString()
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
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)
            // do your logic here...
            selectedImages = images
            val adapter = SelectedImagesAdapter(context!!)
            Log.i("onActivityResult", images[0].path + "...")
            selectedImagesList.layoutManager = GridLayoutManager(context!!, 4)
            selectedImagesList.adapter = adapter
            adapter.setImages(images)
            adapter.notifyDataSetChanged()

        }
        when (requestCode) {
            EasyWayLocation.LOCATION_SETTING_REQUEST_CODE -> easyWayLocation?.onActivityResult(resultCode)
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

    private fun configLocationPremation() {
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
    }
}
