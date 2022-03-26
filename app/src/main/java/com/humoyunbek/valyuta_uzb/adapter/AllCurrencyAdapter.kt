package com.humoyunbek.valyuta_uzb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humoyunbek.valyuta_uzb.R
import com.humoyunbek.valyuta_uzb.databinding.ItemAllBinding
import com.humoyunbek.valyuta_uzb.models.Data
import com.squareup.picasso.Picasso

class AllCurrencyAdapter(var list: ArrayList<Data>,var onMyItemClickListener: OnMyItemClickListener) :
    RecyclerView.Adapter<AllCurrencyAdapter.MyViewHolder>() {
    inner class MyViewHolder(var binding: ItemAllBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Data,position: Int) {
            binding.textCountryName.text = data.code
            binding.rvCalcBtn.setOnClickListener {
                onMyItemClickListener.onMyItemClick(data.code,position)
            }
            binding.allRvSoldText.text =
                if (data.nbu_buy_price.length > 2) "${data.nbu_buy_price}" else "${data.cb_price}"

            binding.allRvSoldText2.text =
                if (data.nbu_cell_price.length > 2) "${data.nbu_cell_price}" else "${data.cb_price}"
            Picasso.get()
                .load("https://flagcdn.com/80x60/${data.code?.substring(0, 2)?.toLowerCase()}.png")
                .into(binding.imgCountry)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            ItemAllBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[position],position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface OnMyItemClickListener{
        fun onMyItemClick(code: String,position: Int)
    }

}