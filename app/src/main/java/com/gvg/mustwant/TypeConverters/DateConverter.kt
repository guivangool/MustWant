package com.gvg.mustwant.TypeConverters

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    val sdf = SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault())
    @TypeConverter
    fun fromStringToDate (value:String?): Date? {
        return if (value == null) null else sdf.parse(value)
        }

    @TypeConverter
    fun fromDateToString (value:Date?): String? {
        return if (value == null) null else sdf.format(value)
    }
}