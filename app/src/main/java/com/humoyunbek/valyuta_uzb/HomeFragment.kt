package com.humoyunbek.valyuta_uzb

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.humoyunbek.valyuta_uzb.adapter.HistoryAdapter
import com.humoyunbek.valyuta_uzb.adapter.PagerAdapter
import com.humoyunbek.valyuta_uzb.databinding.FragmentHomeBinding
import com.humoyunbek.valyuta_uzb.db.MyDatabase
import com.humoyunbek.valyuta_uzb.models.Data
import com.humoyunbek.valyuta_uzb.models.TitleModel
import com.humoyunbek.valyuta_uzb.retrofit.ApiClient
import com.humoyunbek.valyuta_uzb.utils.MySharedPrefarance
import kotlinx.android.synthetic.main.help_main_layout.*
import kotlinx.android.synthetic.main.tab_item_category.view.*
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
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    interface onSomeEventListener {
        fun someEvent(s: Int?)
    }

    var someEventListener: onSomeEventListener? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        someEventListener = try {
            activity as onSomeEventListener
        } catch (e: Exception) {
            throw Exception("$activity must implement onSomeEventListener")
        }
    }

    lateinit var binding: FragmentHomeBinding
    lateinit var categoryList: ArrayList<TitleModel>
    lateinit var allList: ArrayList<Data>
    lateinit var pagerAdapter: PagerAdapter
    lateinit var historyAdapter: HistoryAdapter
    lateinit var listsDatabase: ArrayList<Data>
    var tabSelected = 0

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(context))
        ApiClient.apiInterface.getData().enqueue(object : Callback<List<Data>> {
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                if (response.isSuccessful) {
                    someEventListener?.someEvent(1)
                    MySharedPrefarance.init(context)
                    categoryList = ArrayList()
                    allList = response.body() as ArrayList<Data>
                    var roomList = ArrayList<Data>()
                    val myDatabase = MyDatabase.getInstance(context!!)
                    if (MySharedPrefarance.obektString != null) {
                        val sharedList = MySharedPrefarance.obektString
                        for (i in 0 until sharedList.size) {
                            if (sharedList[i].code == allList[i].code) {
                                if (sharedList[i].cb_price != allList[i].cb_price) {
                                    myDatabase.myDao().addCurrency(allList[i])
                                }
                            }
                        }
                        MySharedPrefarance.obektString = allList
                    } else {
                        MySharedPrefarance.obektString = allList
                    }
                    allList.forEach {
                        categoryList.add(TitleModel(it.code))
                    }
                    pagerAdapter = PagerAdapter(categoryList, requireActivity())
                    binding.viewPager.adapter = pagerAdapter

                    TabLayoutMediator(
                        binding.tabLayout,
                        binding.viewPager,
                        object : TabLayoutMediator.TabConfigurationStrategy {
                            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                                tab.text = categoryList[position].titleName
                            }

                        }).attach()

                    binding.tabLayout.addOnTabSelectedListener(object :
                        TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            tabSelected = tab?.position!!
                            val customview = tab?.customView
                            val name = customview?.text_tab?.text
                            val appDatabase = MyDatabase.getInstance(context!!)
                            listsDatabase = ArrayList()
                            var filterList = ArrayList<Data>()
                            listsDatabase.addAll(appDatabase.myDao().getAllCurrenc())
                            if (name == "") {
                                for (data in listsDatabase) {
                                    if (data.code == "AED") {
                                        filterList.add(data)
                                    }
                                }
                            } else {
                                for (data in listsDatabase) {
                                    if (data.code == name) {
                                        filterList.add(data)
                                    }
                                }
                            }
                            historyAdapter = HistoryAdapter(filterList)
                            binding.historyRv.adapter = historyAdapter
                            customview?.text_tab?.background =
                                resources.getDrawable(R.drawable.selected)
                            customview?.text_tab?.setTextColor(resources.getColor(R.color.white))

                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) {
                            val customView = tab?.customView
                            customView?.text_tab?.background =
                                resources.getDrawable(R.drawable.unselected)
                            customView?.text_tab?.setTextColor(resources.getColor(R.color.un_selected))
                        }

                        override fun onTabReselected(tab: TabLayout.Tab?) {

                        }

                    })

                    binding.viewpagerIndicator.attachToPager(binding.viewPager)


                    val tabCount = binding.tabLayout.tabCount
                    for (i in 0 until tabCount) {
                        val tabView = LayoutInflater.from(context)
                            .inflate(R.layout.tab_item_category, null, false)
                        val tab = binding.tabLayout.getTabAt(i)
                        tab?.customView = tabView
                        tabView.text_tab.text = categoryList[i].titleName
                        if (i == 0) {
                            tabView.text_tab?.background =
                                resources.getDrawable(R.drawable.selected)
                            tabView?.text_tab?.setTextColor(resources.getColor(R.color.white))
                        } else {

                            tabView?.text_tab?.background =
                                resources.getDrawable(R.drawable.unselected)
                            tabView?.text_tab?.setTextColor(resources.getColor(R.color.un_selected))
                        }
                    }

                }
            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })




        return binding.root
    }


    override fun onResume() {
        super.onResume()
        binding.tabLayout.getTabAt(tabSelected)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}