package com.application.intercom.db.convertor

import androidx.room.TypeConverter
import com.application.intercom.db.entity.ServicesCategoryTable
import com.google.gson.Gson

class ServicesCategoryConverters {

    @TypeConverter
    fun sourceToString(model: ServicesCategoryTable?):String?{
        if(model!=null){
            return Gson().toJson(model)
        }
        return null
    }

    @TypeConverter
    fun sourceFromString(value:String?): ServicesCategoryTable?{
        if(value!=null){
            return Gson().fromJson(value, ServicesCategoryTable::class.java)
        }
        return null
    }

}