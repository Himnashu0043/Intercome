package com.application.intercom.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.application.intercom.db.dao.ServicesCategoryDao
import com.application.intercom.db.entity.ServicesCategoryTable
import javax.inject.Singleton

@Database(entities = [ServicesCategoryTable::class], version = 16, exportSchema = true)
//@TypeConverters(ServicesCategoryConverters::class)

@Singleton
abstract class AppDatabase:RoomDatabase() {
    abstract fun servicesDao(): ServicesCategoryDao

    companion object{
        private var instance:AppDatabase?=null

        @Synchronized
        fun getInstance(context:Context):AppDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java,"intercom_database").fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}