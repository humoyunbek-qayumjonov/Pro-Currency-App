package com.humoyunbek.valyuta_uzb.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.humoyunbek.valyuta_uzb.models.Data


@Dao
interface MyDao {
    @Query("select*from Data")
    fun getAllCurrenc(): List<Data>

    @Insert
    fun addCurrency(data: Data)


}