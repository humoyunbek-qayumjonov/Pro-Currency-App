package com.humoyunbek.valyuta_uzb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.humoyunbek.valyuta_uzb.R
import com.humoyunbek.valyuta_uzb.models.Data
import kotlinx.android.synthetic.main.item_spinner.view.*

class SpinnerAdapter(var list:List<Data>):BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView:View =
            convertView?:LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_spinner,parent,false)
        itemView.name_spinner.text = list[position].code
        return itemView
    }
}