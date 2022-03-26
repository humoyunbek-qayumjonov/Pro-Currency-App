package com.humoyunbek.valyuta_uzb.retrofit

import com.humoyunbek.valyuta_uzb.models.Data
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("uz/exchange-rates/json")
    fun getData():Call<List<Data>>
}