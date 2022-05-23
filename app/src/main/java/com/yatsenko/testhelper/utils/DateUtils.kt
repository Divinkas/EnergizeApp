package com.yatsenko.testhelper.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val DEFAULT_DATE_FORMAT = "dd-MM-yyyy/HH:mm:ss"

    @SuppressLint("SimpleDateFormat")
    fun getFormattedDate(time: Long): String {
        try {
            return SimpleDateFormat(DEFAULT_DATE_FORMAT).format(Date(time))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ""
    }
}