package com.haja.haja.View.ui.AddProduct

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haja.haja.R
import com.haja.haja.Service.model.AttributeData
import kotlinx.android.synthetic.main.add_attributes_item.view.*

class AddProductAttributesAdapter() : RecyclerView.Adapter<AddProductAttributesAdapter.ViewHolder>() {

    private var attributes: List<AttributeData?>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_attributes_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return if (attributes == null) 0
        else attributes?.size!!
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val category = attributes?.get(position)
        holder.name.text = category?.name
        holder.value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                attributes?.get(position)?.value = text.toString()
            }
        })
    }

    fun getEnteredAttributesData(): HashMap<String, List<String>> {
        val addedAttributesIDs = ArrayList<String>()
        val addedAttributesValues = ArrayList<String>()
        val map = HashMap<String, List<String>>()
        for (i in attributes?.indices!!) {
            addedAttributesIDs.add(attributes!![i]?.id.toString())
            addedAttributesValues.add(attributes!![i]?.value.toString())
        }
        map["attributes_id[]"] = addedAttributesIDs
        map["attributes_value[]"] = addedAttributesValues
        Log.i("attributes_id[]", map.get("attributes_id[]").toString())
        Log.i("attributes_value[]", map.get("attributes_value[]").toString())
        return map
    }

    fun setAttributes(attributes: List<AttributeData>) {
        this.attributes = attributes
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.addProAttributeName
        val value = itemView.addProAttributeValue
    }
}