package br.com.fcr.gastin.data.database.resource

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromDoubleArray(array: Array<DoubleArray>): String {
        return gson.toJson(array)
    }

    @TypeConverter
    fun toDoubleArray(serialized: String): Array<DoubleArray> {
        return gson.fromJson(serialized, Array<DoubleArray>::class.java)
    }
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}