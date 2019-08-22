package com.haja.haja.View.ui.SubCategoriesScreen

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.R
import com.haja.haja.model.CategoriesData
import com.infovass.lawyerskw.lawyerskw.Utils.ui.CustomProgressBar
import kotlinx.android.synthetic.main.sub_categories_fragment.*

class SubCategoriesFragment : Fragment() {

    companion object {
        fun newInstance(categoryId: Int) = SubCategoriesFragment().apply {
            arguments = Bundle().apply {
                putInt("categoryId", categoryId)
            }
        }
    }

    private lateinit var viewModel: SubCategoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub_categories_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val categoryId = arguments!!.getInt("categoryId")
        val progress = CustomProgressBar.showProgressBar(context!!)
        progress.show()
        Log.i("sub_categoryId", "$categoryId")
        viewModel = ViewModelProviders.of(this).get(SubCategoriesViewModel::class.java)
        viewModel.ssetParentId(categoryId)
        viewModel.getCategories().observe(this, Observer { subCategories ->
            progress.dismiss()
            if (subCategories != null) {
                if (subCategories.result == true)
                    initRecycler(subCategories.data)
                else
                    Toast.makeText(context, subCategories.errorMessage, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(context, resources.getString(R.string.error), Toast.LENGTH_SHORT).show()
        })
    }

    private fun initRecycler(data: List<CategoriesData?>?) {
        subCategoriesList.apply {
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL, false
            )
            adapter = SubCategoriesAdapter(data as List<CategoriesData>, fragmentManager)
        }

    }

}