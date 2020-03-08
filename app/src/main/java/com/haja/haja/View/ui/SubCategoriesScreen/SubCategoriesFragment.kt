package com.haja.haja.View.ui.SubCategoriesScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.haja.haja.R
import com.haja.haja.model.CategoriesData
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.sub_categories_fragment.*

class SubCategoriesFragment : Fragment() {

    companion object {
        fun newInstance(categoryId: Int, name: String?) = SubCategoriesFragment().apply {
            arguments = Bundle().apply {
                putInt("categoryId", categoryId)
                putString("categoryName", name)
            }
        }
    }

    private var viewModel: SubCategoriesViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub_categories_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val categoryId = arguments!!.getInt("categoryId")
        val categoryName = arguments!!.getString("categoryName")
        activity?.appBarTitle?.text = categoryName
        activity?.categoriesBarBack?.visibility = View.VISIBLE
        activity?.categoriesBarMenu?.visibility = View.GONE

        val progress = CustomProgressBar.showProgressBar(context!!)
        progress.show()
        Log.i("sub_categoryId", "$categoryId")
        if (viewModel == null)
            viewModel = ViewModelProviders.of(this).get(SubCategoriesViewModel::class.java)

        viewModel?.setParentId(categoryId)
        viewModel?.getCategories()?.observe(this, Observer { subCategories ->
            progress.dismiss()
            if (subCategories != null) {
                if (subCategories.result == true)
                {
                    subCategories.data?.let { initCategoryAds(it, categoryId) }
                }
                else
                    Toast.makeText(context, subCategories.errorMessage, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(
                    context,
                    resources.getString(R.string.error),
                    Toast.LENGTH_SHORT
                ).show()
        })
    }

    private fun initCategoryAds(
        data: List<CategoriesData?>,
        categoryId: Int
    ) {

        viewModel?.getAds(categoryId)?.observe(this, Observer {
            if (it != null) {
                val newData =  ArrayList<CategoriesData?>()
                newData.addAll(data)
                for (i in data.indices){
                    if ( (i / 6).toString().length == 1){
                        Log.e("newData/length","${(i / 6).toString().length }")
                        Log.e("newData/string","${(i / 6)}")
                        Log.e("newData/int","$i")
                        val catData  = CategoriesData()
                        catData.childAds = it.data
                        newData.add(i + 1 , catData)
                    }
                }
                initRecycler(newData)

            }
        })
    }

    private fun initRecycler(data: List<CategoriesData?>?) {
        subCategoriesList.apply {
            layoutManager = GridLayoutManager(
                context, 3
            )
            adapter = SubCategoriesAdapter(data as List<CategoriesData>, fragmentManager, context!!)
        }

    }

}
