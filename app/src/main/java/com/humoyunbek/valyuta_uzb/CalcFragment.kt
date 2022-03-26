package com.humoyunbek.valyuta_uzb

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.humoyunbek.valyuta_uzb.adapter.SpinnerAdapter
import com.humoyunbek.valyuta_uzb.databinding.FragmentCalcBinding
import com.humoyunbek.valyuta_uzb.models.Data
import com.humoyunbek.valyuta_uzb.retrofit.ApiClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.help_main_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class CalcFragment : Fragment() {

    interface onSomeEventListener {
        fun someEvent(s: Int?)
    }

    var someEventListener: HomeFragment.onSomeEventListener? = null


    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        someEventListener = try {
            activity as HomeFragment.onSomeEventListener
        } catch (e: Exception) {
            throw Exception("$activity must implement onSomeEventListener")
        }
    }

    lateinit var binding: FragmentCalcBinding
    lateinit var list: ArrayList<Data>
    lateinit var sharedPreferences: SharedPreferences
    var isChange = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalcBinding.inflate(LayoutInflater.from(context))
        sharedPreferences =
            activity?.getSharedPreferences("Currensy_online", Context.MODE_PRIVATE)!!
        val key = sharedPreferences.getInt("pos", -1)

        ApiClient.apiInterface.getData().enqueue(object : Callback<List<Data>> {
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                if (response.isSuccessful) {
                    someEventListener?.someEvent(1)
                    list = response.body() as ArrayList<Data>
                    list.add(Data(0, "1", "UZB", "", "", "", "o'zbekiston so'mi"))
                    val spinnerAdapter = SpinnerAdapter(list)
                    binding.title.adapter = spinnerAdapter
                    binding.title2.adapter = spinnerAdapter
                    if (key != null) {
                        binding.title.setSelection(key)
                    }
                    binding.title.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                Picasso.get()
                                    .load(
                                        "https://flagcdn.com/160x120/${
                                            list[position].code?.substring(0, 2)?.toLowerCase()
                                        }.png"
                                    )
                                    .into(binding.image)
                                binding.cardSell.text =
                                    if (list[position].nbu_buy_price!!.length > 2) "${list[position].nbu_buy_price} UZS" else "${list[position].cb_price} UZS"

                                binding.cardBuy.text =
                                    if (list[position].nbu_cell_price!!.length > 2) "${list[position].nbu_cell_price} UZS" else "${list[position].cb_price} UZS"

                                binding.edit.addTextChangedListener(object : TextWatcher {
                                    override fun beforeTextChanged(
                                        s: CharSequence?,
                                        start: Int,
                                        count: Int,
                                        after: Int
                                    ) {

                                    }

                                    override fun onTextChanged(
                                        s: CharSequence?,
                                        start: Int,
                                        before: Int,
                                        count: Int
                                    ) {
                                        if (s.toString() != "") {
                                            val edit_hisob = s.toString().toFloat()
                                            val spin1 = binding.title.selectedItemPosition
                                            val spin2 = binding.title2.selectedItemPosition
                                            val hisob = list[spin1].cb_price.toFloat()
                                            val hisob2 = list[spin2].cb_price.toFloat()
                                            var natija = (hisob * edit_hisob) / hisob2
                                            binding.result.text = "$natija ${list[spin2].code}"
                                        } else binding.result.text = ""
                                    }

                                    override fun afterTextChanged(s: Editable?) {

                                    }

                                })
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                        }

                    binding.title2.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                Picasso
                                    .get()
                                    .load(
                                        "https://flagcdn.com/160x120/${
                                            list[position].code?.substring(0, 2)?.toLowerCase()
                                        }.png"
                                    )
                                    .into(binding.image2)
                            }

                        }

                    binding.change.setOnClickListener {
                        val pos1 = binding.title.selectedItemPosition
                        val pos2 = binding.title2.selectedItemPosition
                        isChange = !isChange
                        binding.title.setSelection(pos2)
                        binding.title2.setSelection(pos1)

                        if (binding.edit.text.isNotEmpty()) {
                            val s = binding.edit.text.toString()
                            val edit_hisob = s.toString().toFloat()
                            val spin1 = binding.title.selectedItemPosition
                            val spin2 = binding.title2.selectedItemPosition
                            val hisob = list[spin1].cb_price.toFloat()
                            val hisob2 = list[spin2].cb_price.toFloat()
                            var natija = (hisob * edit_hisob) / hisob2
                            binding.result.text = "$natija ${list[spin2].code}"
                        }
                    }


                }
            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {

            }

        })
        return binding.root
    }

}