package com.ivyclub.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.sql.Date
import java.util.*

class RoomConverters {
    @TypeConverter
    fun longListToJson(value: List<Long>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToLongList(value: String) = Gson().fromJson(value, Array<Long>::class.java).toList()

    @TypeConverter
    fun stringListToJson(value: List<String>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToStringList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun mapToJson(value: Map<String,String>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToMap(value: String): Map<String,String> {
        val mapType = object : TypeToken<Map<String?, String?>?>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun dateToLong(value: Date) = value.time

    @TypeConverter
    fun longToDate(value: Long) = Date(value)
}