package com.haja.haja.View.ui.MainCategoriesScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.model.CategoriesData
import kotlinx.android.synthetic.main.main_categories_item.view.*
import android.os.Handler
import com.haja.haja.R

class MainCategoriesAdapter(
    private val parents: List<CategoriesData>,
    private val context: Context,
    private val fragmentManager: FragmentManager?
) :
    RecyclerView.Adapter<MainCategoriesAdapter.ViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_categories_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return parents.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val parent = parents[position]
        holder.textView.text = parent.name


        val childLayoutManager = LinearLayoutManager(
            holder.recyclerView.context, LinearLayout.HORIZONTAL, false
        )
        childLayoutManager.initialPrefetchItemCount = 4
        holder.recyclerView.apply {
            layoutManager = childLayoutManager
            adapter = MainChildCatAdapter(parents[position].childCategories,context,fragmentManager)
            setRecycledViewPool(viewPool)
        }

        val childAdsLayoutManager = LinearLayoutManager(
            holder.adsRecyclerView.context, LinearLayout.HORIZONTAL, false
        )
        childAdsLayoutManager.initialPrefetchItemCount = 2
        holder.adsRecyclerView.apply {
            layoutManager = childAdsLayoutManager
            adapter = MainChildAdsAdapter(parents[position].childAds , context)
            AutoScrool(holder, adapter as MainChildAdsAdapter)
            setRecycledViewPool(viewPool)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.mainChildCategories
        val adsRecyclerView: RecyclerView = itemView.mainChildAds
        val textView: TextView = itemView.mainCategoryName
    }

    fun AutoScrool(
        holder: ViewHolder,
        adapter: MainChildAdsAdapter
    ) {
        val speedScroll = 3000
        val handler = Handler()
        val runnable = object : Runnable {
            var count = 0
            var flag = true
            override fun run() {
                if (count < adapter.itemCount) {
                    if (count == adapter.itemCount - 1) {
                        flag = false
                    } else if (count == 0) {
                        flag = true
                    }
                    if (flag)
                        count++
                    else
                        count--

                    holder.adsRecyclerView.smoothScrollToPosition(count)
                    handler.postDelayed(this, speedScroll.toLong())
                }
            }
        }

        handler.postDelayed(runnable, speedScroll.toLong())
    }
}