package com.humoyunbek.valyuta_uzb

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.humoyunbek.valyuta_uzb.adapter.AllCurrencyAdapter
import com.humoyunbek.valyuta_uzb.databinding.ActivitySearchBinding
import com.humoyunbek.valyuta_uzb.models.Data
import com.humoyunbek.valyuta_uzb.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity() {
    lateinit var binding:ActivitySearchBinding
    lateinit var list:ArrayList<Data>
    lateinit var allCurrencyAdapter: AllCurrencyAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchView.setQuery("",true)
        binding.searchView.isFocusable = true
        binding.searchView.isIconified = false
        binding.searchView.requestFocusFromTouch()
        list = ArrayList()
        var someEventListener: HomeFragment.onSomeEventListener? = null

        ApiClient.apiInterface.getData().enqueue(object : Callback<List<Data>> {
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                if (response.isSuccessful){

                    list = response.body() as ArrayList<Data>
                    Log.d(ContentValues.TAG, "onResponse: ${response.body()}")
                    allCurrencyAdapter = AllCurrencyAdapter(list, object : AllCurrencyAdapter.OnMyItemClickListener {
                        override fun onMyItemClick(code: String, position: Int) {
                            val intent = Intent(this@SearchActivity,MainActivity::class.java)
                            intent.putExtra("key1",4)
                            startActivity(intent)

                            sharedPreferences = getSharedPreferences("Currensy_online", Context.MODE_PRIVATE)!!
                            editor = sharedPreferences.edit()
                            editor.putInt("pos",position)
                            editor.commit()
                        }


                    })
                    binding.rvSearch.adapter = allCurrencyAdapter

                    binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
                        androidx.appcompat.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            var sortedList = ArrayList<Data>()
                            for (data in list) {
                                for (i in data.code.indices){
                                    if (data.code.subSequence(0,i).toString().lowercase(Locale.getDefault()) == newText!!.toLowerCase()){
                                        sortedList.add(data)
                                    }
                                }
                            }
                            allCurrencyAdapter = AllCurrencyAdapter(sortedList,
                                object : AllCurrencyAdapter.OnMyItemClickListener {
                                    override fun onMyItemClick(code: String, position: Int) {
                                        val intent = Intent(this@SearchActivity,MainActivity::class.java)
                                        intent.putExtra("key1",4)
                                        startActivity(intent)
                                        sharedPreferences = getSharedPreferences("Currensy_online", Context.MODE_PRIVATE)!!
                                        editor = sharedPreferences.edit()
                                        editor.putInt("pos",position)
                                        editor.commit()
                                    }


                                })
                            binding.rvSearch.adapter = allCurrencyAdapter
                            return true
                        }

                    })
                }
            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: ${t.message}")
            }

        })

    }

}