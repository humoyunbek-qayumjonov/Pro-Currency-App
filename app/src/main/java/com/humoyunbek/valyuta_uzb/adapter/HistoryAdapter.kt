package com.humoyunbek.valyuta_uzb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.humoyunbek.valyuta_uzb.databinding.ItemTarixBinding
import com.humoyunbek.valyuta_uzb.models.Data

class HistoryAdapter(var list:ArrayList<Data>):RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {
    inner class MyViewHolder(var binding:ItemTarixBinding):RecyclerView.ViewHolder(binding.root){
        fun onBind(data:Data){
            binding.dataTextRv.text =data.date.substring(0, 10)
            binding.rvSoldText.text = data.cb_price
            binding.rvSoldText2.text = data.cb_price
            binding.timeTextRv.text = data.date.substring(11)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return MyViewHolder(
            ItemTarixBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[position])
    }
    override fun getItemCount(): Int {
        return list.size
    }
}