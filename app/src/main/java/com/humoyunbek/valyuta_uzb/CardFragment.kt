package com.humoyunbek.valyuta_uzb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.humoyunbek.valyuta_uzb.databinding.FragmentCardBinding
import com.humoyunbek.valyuta_uzb.models.Data
import com.humoyunbek.valyuta_uzb.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [CardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }
    lateinit var binding: FragmentCardBinding
    lateinit var list:ArrayList<Data>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCardBinding.inflate(LayoutInflater.from(context))
        ApiClient.apiInterface.getData().enqueue(object :Callback<List<Data>>{
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                if (response.isSuccessful){
                    list = response.body() as ArrayList<Data>
                    list?.forEach {
                        if (it.code == param1){
                            binding.soldValyuta.text =
                                if (it.nbu_cell_price.length>2) "${it.nbu_cell_price}" else "${it.cb_price}"
                            binding.soldValyuta2.text =
                                if (it.nbu_buy_price.length>2) "${it.nbu_buy_price}" else "${it.cb_price}"
                            val substring = it.date.substring(0, 10)
                            binding.cardTime.text = substring
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {

            }

        })
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            CardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}