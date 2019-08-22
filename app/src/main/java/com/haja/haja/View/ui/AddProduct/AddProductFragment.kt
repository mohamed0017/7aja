package com.haja.haja.View.ui.AddProduct

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.haja.haja.OnCategoryItemClick
import com.haja.haja.R
import com.haja.haja.model.CategoriesData
import kotlinx.android.synthetic.main.add_product_fragment.*
import kotlinx.android.synthetic.main.add_products_categories.view.*
import androidx.recyclerview.widget.RecyclerView
import android.view.animation.AnimationUtils
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import android.content.Intent
import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.haja.haja.Service.model.AttributeData
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.USERID
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddProductFragment : Fragment(), OnCategoryItemClick {

    override fun onClick(possion: Int, itemData: CategoriesData) {
        if (itemData.countSubCat == 0) {
            setupAttributesList(itemData.id)
            categoriesDialog.dismiss()
            selectedCategory = itemData.id!!
            selectCategory.text = itemData.name
        } else
            getNextCategories(itemData.id!!)
    }

    companion object {
        fun newInstance() = AddProductFragment()
    }

    private lateinit var viewModel: AddProductViewModel
    private lateinit var adapter: AddProductCatAdapter
    private var attributesAdapter = AddProductAttributesAdapter()

    private val categoriesDialog = AddProductCategoriesDialog()
    private var selectedImages = ArrayList<Image>()
    private var selectedCategory = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_product_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddProductViewModel::class.java)
        adapter = AddProductCatAdapter(this)
        val progress = CustomProgressBar.showProgressBar(context!!)

        uploadImages.setOnClickListener {
            openGallery()
        }

        selectCategory.setOnClickListener {
            categoriesDialog.show(context!!)
            categoriesDialog.getDialogView().addProductCategoriesList.layoutManager = LinearLayoutManager(context!!)
            categoriesDialog.getDialogView().addProductCategoriesList.adapter = adapter
            viewModel.setParentId(0)
            viewModel.getCategories().observe(this, Observer { categories ->
                if (categories != null) {
                    adapter.setCategoriesList(categories.data as List<CategoriesData>)
                    runLayoutAnimation(categoriesDialog.getDialogView().addProductCategoriesList)
                } else {

                }
            })
        }

        addProductBut.setOnClickListener {
            if (isValidProductData()) {
                progress.show()
                viewModel.setProductAttributes(attributesAdapter.getEnteredAttributesData())
                viewModel.setImages(getSelectedImages())
                viewModel.setProductData(getProductData())
                viewModel.addProduct().observe(this, Observer { products ->
                    progress.dismiss()
                    if (products != null) {
                        makeToast(context!!, products.errorMesage.toString())
                    } else {
                        makeToast(context!!, resources.getString(R.string.error))
                    }
                })
            }
        }
    }

    private fun getProductData(): HashMap<String, String> {
        val map = HashMap<String, String>()
        map["name"] = addProNameAr.text.toString()
        map["name_en"] = addProNameEn.text.toString()
        map["price"] = addProPrice.text.toString()
        map["discount"] = addProDiscount.text.toString()
        map["quantity"] = addProQuantity.text.toString()
        map["description"] = addProDescriptionAr.text.toString()
        map["description_en"] = addProDescriptionEn.text.toString()
        map["tags"] = addProTagsAr.text.toString()
        map["tags_en"] = addProTags.text.toString()

        map["cat_id"] = selectedCategory.toString()
        map["type"] = "1"
        map["is_special"] = "0"
        map["user_id"] = "167"
        return map
    }

    private fun isValidProductData(): Boolean {
        return if (selectedImages.isNullOrEmpty()) {
            makeToast(context!!, resources.getString(R.string.select_img))
            false
        } else if (selectedCategory == 0) {
            makeToast(context!!, resources.getString(R.string.select_cat))
            false
        } else
            true
    }

    private fun getSelectedImages(): List<MultipartBody.Part> {
        val images = ArrayList<MultipartBody.Part>()
        for (index in selectedImages.indices) {
            val file = File(selectedImages[index].path)
            val imageBody = RequestBody.create(MediaType.parse("image/*"), file)
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
                adapter.setCategoriesList(categories.data as List<CategoriesData>)
                runLayoutAnimation(categoriesDialog.getDialogView().addProductCategoriesList)
            } else {

            }
        })
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right)
        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    private fun setupAttributesList(catId: Int?) {
        addProductAttributes.layoutManager = LinearLayoutManager(context!!)
        // TODO replace 11 with catId
        viewModel.getCategoryAttributes(11).observe(this, Observer { attributes ->
            if (attributes != null) {
                if (attributes.result == true) {
                    addProductAttributes.adapter = attributesAdapter
                    attributesAdapter.setAttributes(attributes.data as List<AttributeData>)
                } else
                    makeToast(context!!, attributes.errorMesage.toString())
            } else
                makeToast(context!!, resources.getString(R.string.error))
        })
    }
}
