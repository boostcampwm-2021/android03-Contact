package com.ivyclub.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class RoomConverters {
    @TypeConverter
    fun listToJson(value: List<String>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value,Array<String>::class.java).toList()

    @TypeConverter
    fun mapToJson(value: Map<String,String>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToMap(value: String): Map<String,String> {
        val mapType = object : TypeToken<Map<String?, String?>?>() {}.type
        return Gson().fromJson(value, mapType)
    }
}