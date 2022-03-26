package com.humoyunbek.valyuta_uzb

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.humoyunbek.valyuta_uzb.adapter.AllCurrencyAdapter
import com.humoyunbek.valyuta_uzb.databinding.FragmentAllBinding
import com.humoyunbek.valyuta_uzb.models.Data
import com.humoyunbek.valyuta_uzb.retrofit.ApiClient
import kotlinx.android.synthetic.main.help_main_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllFragment : Fragment() {

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
    lateinit var binding: FragmentAllBinding
    lateinit var allCurrencyAdapter:AllCurrencyAdapter
    lateinit var allList:ArrayList<Data>
    lateinit var sharedPreferences:SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllBinding.inflate(LayoutInflater.from(context))
        ApiClient.apiInterface.getData().enqueue(object : Callback<List<Data>> {
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                if (response.isSuccessful){
                    someEventListener?.someEvent(2)
                    allList = ArrayList()
                    allList = response.body() as ArrayList<Data>
                    Log.d(TAG, "onResponse: ${response.body()}")
                    allCurrencyAdapter = AllCurrencyAdapter(allList, object : AllCurrencyAdapter.OnMyItemClickListener {
                        override fun onMyItemClick(code: String, position: Int) {
//                            bottom_bar.selectedItemId = R.id.calcFragment
////                            findNavController().navigate(R.id.calcFragment, bundleOf("code" to position))
                            someEventListener?.someEvent(4)
                            sharedPreferences =
                                activity?.getSharedPreferences("Currensy_online",Context.MODE_PRIVATE)!!
                            editor = sharedPreferences.edit()
                            editor.putInt("pos",position)
                            editor.commit()



                        }

                    })
                    binding.allRv.adapter = allCurrencyAdapter

                }
            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
        return binding.root
    }


}