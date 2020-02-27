package com.example.myapplication

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataTypeConverters{
    val gson = Gson()

    @TypeConverter
    fun stringToList(string: String): List<String>{
        val type = object: TypeToken<List<String>>(){}.type
        return gson.fromJson(string, type)
    }

    @TypeConverter
    fun fromListString(stringList: List<String>): String{
        val type = object: TypeToken<List<String>>(){}.type
        return gson.toJson(stringList, type)
    }

}