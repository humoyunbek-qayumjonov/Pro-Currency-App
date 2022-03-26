package com.humoyunbek.valyuta_uzb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.humoyunbek.valyuta_uzb.models.Data

@Database(entities = [Data::class], version = 1)
abstract class MyDatabase: RoomDatabase() {
    abstract fun myDao():MyDao

    companion object{
        private var instance:MyDatabase? = null

        @Synchronized
        fun getInstance(context: Context):MyDatabase{
            if (instance == null){
                instance = Room.databaseBuilder(context,MyDatabase::class.java,"currency_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}