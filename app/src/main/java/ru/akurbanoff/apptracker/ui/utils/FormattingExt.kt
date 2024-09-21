package ru.akurbanoff.apptracker.ui.utils

import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun formatTime(hour: Int, minute: Int): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val time = LocalTime.of(hour, minute)
    return time.format(formatter)
}

fun formatSecondsToTime(seconds: Int?): String {
    if (seconds == null) return "00:00"
    val smp = SimpleDateFormat("HH:mm")
    val data = Date((seconds * 1000).toLong())
    return smp.format(data)
}
