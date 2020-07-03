package com.haja.hja.View.ui.AddProduct

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haja.hja.R
import com.haja.hja.Service.model.AttributeData
import kotlinx.android.synthetic.main.add_attributes_item.view.*

class AddProductAttributesAdapter() : RecyclerView.Adapter<AddProductAttributesAdapter.ViewHolder>() {

    private var attributes: ArrayList<AttributeData>? = null

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

    fun getEnteredAttributesData(): HashMap<String, String> {
        val map = HashMap<String, String>()
        if (attributes != null){
            for (i in attributes?.indices!!) {
                map["attributes[${attributes!![i]?.id}]"] = attributes!![i]?.value.toString()
            }
        }
        return map
    }

    fun setAttributes(attributes: ArrayList<AttributeData>) {
        this.attributes = attributes
    }

    fun clearAttributes(){
        attributes?.clear()
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.addProAttributeName
        val value = itemView.addProAttributeValue
    }
}