package com.haja.haja.View.ui.SearchScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.haja.haja.R
import com.haja.haja.Service.model.SearchRequest
import com.haja.haja.Utils.SharedPreferenceUtil
import com.haja.haja.Utils.USERID
import com.haja.haja.Utils.inTransaction
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.appBarTitle?.text = resources.getString(R.string.search_but_title)
        activity?.categoriesBarBack?.visibility = View.VISIBLE
        activity?.categoriesBarMenu?.visibility = View.GONE

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

       searchBut.setOnClickListener {
           if (isValidSearchData()) {
               fragmentManager?.inTransaction {
                   replace(R.id.mainContainer, SearchResultProductsFragment.newInstance(getSearchData()))
                       .addToBackStack("searchScreen")
               }
           }
       }
    }

    private fun isValidSearchData(): Boolean {
        val searchWord = searchEditText.text.toString()
        val searchFrom = priceFromEditText.text.toString()
        val searchTo = priceToEditText.text.toString()
        return when {
            searchWord.isEmpty() -> {
                searchEditText.error = resources.getString(R.string.requird)
                false
            }
           // searchFrom.isEmpty() -> false
           // searchTo.isEmpty() -> false
            else -> true
        }
    }

    private fun getSearchData(): SearchRequest {
        val search = SearchRequest()
        search.kwSearch = searchEditText.text.toString()
        search.fromPrice = priceFromEditText.text.toString()
        search.toPrice = priceToEditText.text.toString()
        search.userId = SharedPreferenceUtil(context!!).getString(USERID,"0")
        return search
    }

}
