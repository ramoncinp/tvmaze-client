package com.ramoncinp.tvmaze.data.formatter

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DATE_PATTERN = "yyyy-MM-dd"

fun Date.formatDate(): String {
    return SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(this)
}
